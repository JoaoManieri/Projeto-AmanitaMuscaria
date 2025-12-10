package br.com.manieri.amanitamuscaria.ui.novaEntrada

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.manieri.amanitamuscaria.domain.models.VehicleEntry
import br.com.manieri.amanitamuscaria.domain.usecases.SaveVehicleEntryUseCase
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
import java.time.Instant

class NovaEntradaViewModel(
    private val saveVehicleEntryUseCase: SaveVehicleEntryUseCase,
    private val errorHandler: ErrorHandler
) : ViewModel() {

    private val _state = MutableStateFlow(VehicleFormState())
    val state: StateFlow<VehicleFormState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<UiEvent>(replay = 0, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val events: SharedFlow<UiEvent> = _events.asSharedFlow()

    fun onFieldChange(field: FormField, value: String) {
        _state.update { current ->
            current.copy(
                fieldErrors = current.fieldErrors - field
            ).let {
                when (field) {
                    FormField.PLATE -> it.copy(plate = value.uppercase())
                    FormField.BRAND -> it.copy(brand = value)
                    FormField.MODEL -> it.copy(model = value)
                    FormField.YEAR -> it.copy(year = value.filter { ch -> ch.isDigit() })
                    FormField.COLOR -> it.copy(color = value)
                    FormField.MILEAGE -> it.copy(mileage = value.filter { ch -> ch.isDigit() })
                    FormField.CUSTOMER -> it.copy(customerName = value)
                    FormField.NOTES -> it.copy(notes = value)
                }
            }
        }
    }

    fun addPhoto(uri: String) {
        _state.update { it.copy(photos = it.photos + uri) }
    }

    fun removePhoto(uri: String) {
        _state.update { it.copy(photos = it.photos.filterNot { current -> current == uri }) }
    }

    fun save() {
        val validationErrors = validate()
        if (validationErrors.isNotEmpty()) {
            _state.update { it.copy(fieldErrors = validationErrors) }
            return
        }

        val current = _state.value
        val entry = VehicleEntry(
            plate = current.plate.trim(),
            brand = current.brand.trim(),
            model = current.model.trim(),
            year = current.year.toInt(),
            color = current.color.trim(),
            mileage = current.mileage.toInt(),
            customerName = current.customerName.trim(),
            notes = current.notes.trim(),
            createdAt = Instant.now(),
            photos = current.photos
        )

        viewModelScope.launch {
            _state.update { it.copy(isSaving = true) }
            try {
                saveVehicleEntryUseCase(entry)
                _events.tryEmit(UiEvent.Saved("Entrada registrada com sucesso."))
                _state.value = VehicleFormState()
            } catch (e: Exception) {
                errorHandler.handle(e, "Não foi possível salvar a entrada do veículo.")
            } finally {
                _state.update { it.copy(isSaving = false) }
            }
        }
    }

    private fun validate(): Map<FormField, String> {
        val errors = mutableMapOf<FormField, String>()
        val current = _state.value

        if (current.plate.isBlank()) errors[FormField.PLATE] = "Placa é obrigatória"
        if (current.brand.isBlank()) errors[FormField.BRAND] = "Marca é obrigatória"
        if (current.model.isBlank()) errors[FormField.MODEL] = "Modelo é obrigatório"
        val yearInt = current.year.toIntOrNull()
        if (yearInt == null || yearInt < 1900 || yearInt > 2100) errors[FormField.YEAR] = "Ano inválido"
        val mileageInt = current.mileage.toIntOrNull()
        if (mileageInt == null || mileageInt < 0) errors[FormField.MILEAGE] = "Quilometragem inválida"
        if (current.color.isBlank()) errors[FormField.COLOR] = "Cor é obrigatória"
        if (current.customerName.isBlank()) errors[FormField.CUSTOMER] = "Nome do cliente é obrigatório"

        return errors
    }
}

sealed class UiEvent {
    data class Saved(val message: String) : UiEvent()
}
