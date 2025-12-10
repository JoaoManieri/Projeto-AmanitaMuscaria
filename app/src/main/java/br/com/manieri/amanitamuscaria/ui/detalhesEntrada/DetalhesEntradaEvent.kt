package br.com.manieri.amanitamuscaria.ui.detalhesEntrada

import java.util.UUID

sealed class DetalhesEntradaEvent {
    data class Load(val id: UUID) : DetalhesEntradaEvent()
    data class UpdateField(val field: VehicleField, val value: String) : DetalhesEntradaEvent()
    data class AddPhoto(val uri: String) : DetalhesEntradaEvent()
    data class RemovePhoto(val uri: String) : DetalhesEntradaEvent()
    data object Save : DetalhesEntradaEvent()
}

enum class VehicleField {
    PLATE,
    BRAND,
    MODEL,
    YEAR,
    COLOR,
    MILEAGE,
    CUSTOMER,
    NOTES
}
