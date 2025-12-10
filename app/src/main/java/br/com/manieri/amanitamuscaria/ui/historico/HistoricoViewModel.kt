package br.com.manieri.amanitamuscaria.ui.historico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.manieri.amanitamuscaria.domain.usecases.DeleteVehicleEntryUseCase
import br.com.manieri.amanitamuscaria.domain.usecases.GetVehicleEntriesUseCase
import br.com.manieri.amanitamuscaria.domain.usecases.SearchVehicleEntriesUseCase
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

class HistoricoViewModel(
    private val getVehicleEntriesUseCase: GetVehicleEntriesUseCase,
    private val searchVehicleEntriesUseCase: SearchVehicleEntriesUseCase,
    private val deleteVehicleEntryUseCase: DeleteVehicleEntryUseCase,
    private val errorHandler: ErrorHandler
) : ViewModel() {

    private val _state = MutableStateFlow(HistoricoUiState(isLoading = true))
    val state: StateFlow<HistoricoUiState> = _state.asStateFlow()

    private val _events = MutableSharedFlow<HistoricoEvent>(replay = 0, extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val events: SharedFlow<HistoricoEvent> = _events.asSharedFlow()

    init {
        refresh()
    }

    fun onSearch(query: String) {
        _state.update { it.copy(query = query) }
        viewModelScope.launch {
            try {
                val result = searchVehicleEntriesUseCase(query)
                _state.update { it.copy(items = result, isLoading = false, errorMessage = null) }
            } catch (e: Exception) {
                errorHandler.handle(e, "Erro ao buscar histórico.")
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                val items = getVehicleEntriesUseCase()
                _state.update { it.copy(items = items, isLoading = false, errorMessage = null) }
            } catch (e: Exception) {
                errorHandler.handle(e, "Erro ao carregar histórico.")
                _state.update { it.copy(isLoading = false, errorMessage = "Erro ao carregar.") }
            }
        }
    }

    fun onDelete(id: String) {
        viewModelScope.launch {
            try {
                deleteVehicleEntryUseCase(id)
                _events.tryEmit(HistoricoEvent.ShowMessage("Entrada removida."))
                onSearch(_state.value.query)
            } catch (e: Exception) {
                errorHandler.handle(e, "Erro ao excluir.")
            }
        }
    }

    fun onItemClick(id: String) {
        _events.tryEmit(HistoricoEvent.NavigateToDetail(id))
    }
}
