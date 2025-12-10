package br.com.manieri.amanitamuscaria.ui.detalhesEntrada

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.manieri.amanitamuscaria.domain.models.VehicleEntry
import br.com.manieri.amanitamuscaria.domain.usecases.AddPhotoToEntryUseCase
import br.com.manieri.amanitamuscaria.domain.usecases.GetVehicleEntryByIdUseCase
import br.com.manieri.amanitamuscaria.domain.usecases.RemovePhotoFromEntryUseCase
import br.com.manieri.amanitamuscaria.domain.usecases.UpdateVehicleEntryUseCase
import br.com.manieri.amanitamuscaria.error.ErrorHandler
import br.com.manieri.amanitamuscaria.error.ErrorType
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
import java.util.UUID

class DetalhesEntradaViewModel(
    private val getVehicleEntryByIdUseCase: GetVehicleEntryByIdUseCase,
    private val updateVehicleEntryUseCase: UpdateVehicleEntryUseCase,
    private val addPhotoToEntryUseCase: AddPhotoToEntryUseCase,
    private val removePhotoFromEntryUseCase: RemovePhotoFromEntryUseCase,
    private val errorHandler: ErrorHandler
) : ViewModel() {

    private val _state = MutableStateFlow(DetalhesEntradaUiState(loading = true))
    val state: StateFlow<DetalhesEntradaUiState> = _state.asStateFlow()

    private val _messages = MutableSharedFlow<String>(replay = 0, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val messages: SharedFlow<String> = _messages.asSharedFlow()

    fun onEvent(event: DetalhesEntradaEvent) {
        when (event) {
            is DetalhesEntradaEvent.Load -> loadEntry(event.id)
            is DetalhesEntradaEvent.UpdateField -> updateField(event.field, event.value)
            is DetalhesEntradaEvent.AddPhoto -> addPhoto(event.uri)
            is DetalhesEntradaEvent.RemovePhoto -> removePhoto(event.uri)
            DetalhesEntradaEvent.Save -> save()
        }
    }

    private fun loadEntry(id: UUID) {
        viewModelScope.launch {
            _state.update { it.copy(loading = true, error = null, saved = false) }
            try {
                val entry = getVehicleEntryByIdUseCase(id.toString())
                if (entry == null) {
                    errorHandler.handleType(ErrorType.DatabaseError(), "Entrada não encontrada.")
                    _state.update { it.copy(loading = false, error = ErrorType.DatabaseError()) }
                    return@launch
                }
                _state.update {
                    it.copy(
                        entry = entry,
                        loading = false,
                        fieldValues = entry.toFieldValues(),
                        fieldErrors = emptyMap(),
                        photos = entry.photos,
                        saved = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                errorHandler.handle(e, "Erro ao carregar detalhes.")
                _state.update { it.copy(loading = false, error = ErrorType.DatabaseError(e)) }
            }
        }
    }

    private fun updateField(field: VehicleField, value: String) {
        val sanitized = when (field) {
            VehicleField.YEAR, VehicleField.MILEAGE -> value.filter { it.isDigit() }
            VehicleField.PLATE -> value.uppercase()
            else -> value
        }

        _state.update { current ->
            val currentValues = current.fieldValues + (field to sanitized)
            current.copy(
                fieldValues = currentValues,
                fieldErrors = current.fieldErrors - field,
                saved = false
            )
        }
    }

    private fun addPhoto(uri: String) {
        val id = _state.value.entry?.id ?: return
        viewModelScope.launch {
            try {
                val updated = addPhotoToEntryUseCase(id, uri)
                _state.update {
                    it.copy(
                        entry = updated,
                        photos = updated.photos,
                        saved = false
                    )
                }
            } catch (e: Exception) {
                errorHandler.handle(e, "Não foi possível adicionar a foto.")
            }
        }
    }

    private fun removePhoto(uri: String) {
        val id = _state.value.entry?.id ?: return
        viewModelScope.launch {
            try {
                val updated = removePhotoFromEntryUseCase(id, uri)
                _state.update {
                    it.copy(
                        entry = updated,
                        photos = updated.photos,
                        saved = false
                    )
                }
            } catch (e: Exception) {
                errorHandler.handle(e, "Não foi possível remover a foto.")
            }
        }
    }

    private fun save() {
        val validationErrors = validate()
        if (validationErrors.isNotEmpty()) {
            val messages = validationErrors.values.toList()
            _state.update { it.copy(fieldErrors = validationErrors, error = ErrorType.ValidationError(messages)) }
            errorHandler.handleType(ErrorType.ValidationError(messages), "Revise os campos antes de salvar.")
            return
        }

        val updated = buildEntry() ?: run {
            errorHandler.handleType(ErrorType.DatabaseError(), "Entrada não encontrada para salvar.")
            return
        }

        viewModelScope.launch {
            try {
                updateVehicleEntryUseCase(updated)
                _state.update {
                    it.copy(
                        entry = updated,
                        saved = true,
                        fieldErrors = emptyMap(),
                        error = null,
                        photos = updated.photos
                    )
                }
                _messages.tryEmit("Alterações salvas.")
            } catch (e: Exception) {
                errorHandler.handle(e, "Não foi possível salvar as alterações.")
            }
        }
    }

    private fun validate(): Map<VehicleField, String> {
        val errors = mutableMapOf<VehicleField, String>()
        val values = _state.value.fieldValues

        val plate = values[VehicleField.PLATE].orEmpty().trim()
        val brand = values[VehicleField.BRAND].orEmpty().trim()
        val model = values[VehicleField.MODEL].orEmpty().trim()
        val year = values[VehicleField.YEAR].orEmpty()
        val color = values[VehicleField.COLOR].orEmpty().trim()
        val mileage = values[VehicleField.MILEAGE].orEmpty()
        val customer = values[VehicleField.CUSTOMER].orEmpty().trim()

        if (plate.isBlank()) errors[VehicleField.PLATE] = "Placa é obrigatória"
        if (brand.isBlank()) errors[VehicleField.BRAND] = "Marca é obrigatória"
        if (model.isBlank()) errors[VehicleField.MODEL] = "Modelo é obrigatório"

        val yearInt = year.toIntOrNull()
        if (yearInt == null || yearInt < 1900 || yearInt > 2100) errors[VehicleField.YEAR] = "Ano inválido"

        val mileageInt = mileage.toIntOrNull()
        if (mileageInt == null || mileageInt < 0) errors[VehicleField.MILEAGE] = "Quilometragem inválida"

        if (color.isBlank()) errors[VehicleField.COLOR] = "Cor é obrigatória"
        if (customer.isBlank()) errors[VehicleField.CUSTOMER] = "Nome do cliente é obrigatório"

        return errors
    }

    private fun buildEntry(): VehicleEntry? {
        val current = _state.value
        val entry = current.entry ?: return null
        val values = current.fieldValues

        val plate = values[VehicleField.PLATE].orEmpty().trim().uppercase()
        val brand = values[VehicleField.BRAND].orEmpty().trim()
        val model = values[VehicleField.MODEL].orEmpty().trim()
        val year = values[VehicleField.YEAR].orEmpty().toInt()
        val color = values[VehicleField.COLOR].orEmpty().trim()
        val mileage = values[VehicleField.MILEAGE].orEmpty().toInt()
        val customer = values[VehicleField.CUSTOMER].orEmpty().trim()
        val notes = values[VehicleField.NOTES].orEmpty()

        return entry.copy(
            plate = plate,
            brand = brand,
            model = model,
            year = year,
            color = color,
            mileage = mileage,
            customerName = customer,
            notes = notes,
            createdAt = entry.createdAt.takeIf { it != Instant.EPOCH } ?: Instant.now(),
            photos = current.photos
        )
    }

    private fun VehicleEntry.toFieldValues(): Map<VehicleField, String> = mapOf(
        VehicleField.PLATE to plate,
        VehicleField.BRAND to brand,
        VehicleField.MODEL to model,
        VehicleField.YEAR to year.toString(),
        VehicleField.COLOR to color,
        VehicleField.MILEAGE to mileage.toString(),
        VehicleField.CUSTOMER to customerName,
        VehicleField.NOTES to notes
    )
}
