package br.com.manieri.amanitamuscaria.ui.historico

import br.com.manieri.amanitamuscaria.domain.models.VehicleEntry

data class HistoricoUiState(
    val isLoading: Boolean = false,
    val query: String = "",
    val items: List<VehicleEntry> = emptyList(),
    val errorMessage: String? = null,
    val emptyMessage: String = "Nenhuma entrada encontrada."
)

sealed class HistoricoEvent {
    data class NavigateToDetail(val id: String) : HistoricoEvent()
    data class ShowMessage(val message: String) : HistoricoEvent()
}
