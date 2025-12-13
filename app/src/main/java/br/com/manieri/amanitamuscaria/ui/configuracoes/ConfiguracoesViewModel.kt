package br.com.manieri.amanitamuscaria.ui.configuracoes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.manieri.amanitamuscaria.domain.models.CompanySettings
import br.com.manieri.amanitamuscaria.domain.usecases.GetCompanySettingsUseCase
import br.com.manieri.amanitamuscaria.domain.usecases.SaveCompanySettingsUseCase
import br.com.manieri.amanitamuscaria.error.ErrorHandler
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ConfiguracoesViewModel(
    private val getCompanySettingsUseCase: GetCompanySettingsUseCase,
    private val saveCompanySettingsUseCase: SaveCompanySettingsUseCase,
    private val errorHandler: ErrorHandler
) : ViewModel() {

    private var lastOperation: SettingsOperation = SettingsOperation.Load
    private val _state = MutableStateFlow(CompanySettingsUiState())
    val state: StateFlow<CompanySettingsUiState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<ConfiguracoesUiEvent>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val events: SharedFlow<ConfiguracoesUiEvent> = _events.asSharedFlow()

    init {
        load()
    }

    fun reload() = load()

    private fun load() {
        lastOperation = SettingsOperation.Load
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val current = getCompanySettingsUseCase()
                _state.update {
                    if (current == null) {
                        it.copy(isLoading = false)
                    } else {
                        it.copy(
                            companyName = current.companyName,
                            address = current.address,
                            phone = current.phone,
                            whatsapp = current.whatsapp,
                            logoUri = current.logoUri,
                            cropX = current.cropX,
                            cropY = current.cropY,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                errorHandler.handle(e, "Não foi possível carregar as configurações.")
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    fun onFieldChange(field: SettingsField, value: String) {
        _state.update { current ->
            val clearedErrors = current.copy(fieldErrors = current.fieldErrors - field)
            when (field) {
                SettingsField.COMPANY_NAME -> clearedErrors.copy(companyName = value)
                SettingsField.ADDRESS -> clearedErrors.copy(address = value)
                SettingsField.PHONE -> clearedErrors.copy(phone = value)
                SettingsField.WHATSAPP -> clearedErrors.copy(whatsapp = value)
            }
        }
    }

    fun onLogoSelected(uri: String) {
        _state.update { it.copy(logoUri = uri) }
    }

    fun onCropChanged(x: Float, y: Float) {
        _state.update { it.copy(cropX = x.coerceIn(-1f, 1f), cropY = y.coerceIn(-1f, 1f)) }
    }

    fun save() {
        val errors = validate()
        if (errors.isNotEmpty()) {
            _state.update { it.copy(fieldErrors = errors) }
            _events.tryEmit(ConfiguracoesUiEvent.ShowMessage("Preencha os campos obrigatórios."))
            return
        }

        val current = _state.value
        val settings = CompanySettings(
            companyName = current.companyName.trim(),
            address = current.address.trim(),
            phone = current.phone.trim(),
            whatsapp = current.whatsapp.trim(),
            logoUri = current.logoUri,
            cropX = current.cropX,
            cropY = current.cropY
        )

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            lastOperation = SettingsOperation.Save
            try {
                saveCompanySettingsUseCase(settings)
                _events.tryEmit(ConfiguracoesUiEvent.Saved("Configurações salvas."))
            } catch (e: Exception) {
                errorHandler.handle(e, "Não foi possível salvar as configurações.")
            } finally {
                _state.update { it.copy(isSaving = false) }
            }
        }
    }

    fun retryLast() {
        when (lastOperation) {
            SettingsOperation.Load -> load()
            SettingsOperation.Save -> save()
        }
    }

    private fun validate(): Map<SettingsField, String> {
        val errors = mutableMapOf<SettingsField, String>()
        if (_state.value.companyName.isBlank()) errors[SettingsField.COMPANY_NAME] = "Informe o nome da empresa"
        return errors
    }
}

private enum class SettingsOperation { Load, Save }

data class CompanySettingsUiState(
    val companyName: String = "",
    val address: String = "",
    val phone: String = "",
    val whatsapp: String = "",
    val logoUri: String? = null,
    val cropX: Float = 0f,
    val cropY: Float = 0f,
    val fieldErrors: Map<SettingsField, String> = emptyMap(),
    val isSaving: Boolean = false,
    val isLoading: Boolean = true
)

enum class SettingsField { COMPANY_NAME, ADDRESS, PHONE, WHATSAPP }

sealed class ConfiguracoesUiEvent {
    data class Saved(val message: String) : ConfiguracoesUiEvent()
    data class ShowMessage(val message: String) : ConfiguracoesUiEvent()
}
