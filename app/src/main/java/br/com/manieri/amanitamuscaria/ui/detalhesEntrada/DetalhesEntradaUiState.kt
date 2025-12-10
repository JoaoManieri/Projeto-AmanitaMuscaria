package br.com.manieri.amanitamuscaria.ui.detalhesEntrada

import br.com.manieri.amanitamuscaria.domain.models.VehicleEntry
import br.com.manieri.amanitamuscaria.error.ErrorType

data class DetalhesEntradaUiState(
    val entry: VehicleEntry? = null,
    val loading: Boolean = false,
    val saved: Boolean = false,
    val error: ErrorType? = null,
    val fieldValues: Map<VehicleField, String> = emptyMap(),
    val fieldErrors: Map<VehicleField, String> = emptyMap(),
    val photos: List<String> = emptyList()
)
