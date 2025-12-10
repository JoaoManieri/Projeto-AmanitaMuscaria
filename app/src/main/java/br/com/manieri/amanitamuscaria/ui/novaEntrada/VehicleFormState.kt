package br.com.manieri.amanitamuscaria.ui.novaEntrada

data class VehicleFormState(
    val plate: String = "",
    val brand: String = "",
    val model: String = "",
    val year: String = "",
    val color: String = "",
    val mileage: String = "",
    val customerName: String = "",
    val notes: String = "",
    val photos: List<String> = emptyList(),
    val isSaving: Boolean = false,
    val fieldErrors: Map<FormField, String> = emptyMap()
)

enum class FormField {
    PLATE, BRAND, MODEL, YEAR, COLOR, MILEAGE, CUSTOMER, NOTES
}
