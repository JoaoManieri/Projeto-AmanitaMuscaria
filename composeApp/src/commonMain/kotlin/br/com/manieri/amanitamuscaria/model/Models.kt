package br.com.manieri.amanitamuscaria.model

import kotlinx.serialization.Serializable

@Serializable
data class Vehicle(
    val plate: String,
    val brand: String,
    val model: String,
    val year: Int,
    val color: String,
    val mileage: Int,
)

@Serializable
data class Client(
    val name: String,
    val phone: String,
    val email: String? = null,
    val document: String? = null,
)

@Serializable
data class InspectionPhoto(
    val id: String,
    val region: String,
    val url: String,
    val timestampLabel: String,
    val bytes: ByteArray? = null,
)

@Serializable
enum class ServiceStatus {
    IN_PROGRESS,
    WAITING_PICKUP,
    COMPLETED,
}

@Serializable
data class Service(
    val id: String,
    val plate: String,
    val vehicle: Vehicle,
    val client: Client,
    val status: ServiceStatus,
    val entryDateLabel: String,
    val exitDateLabel: String? = null,
    val observations: String,
    val inspectionPhotos: List<InspectionPhoto> = emptyList(),
    val signature: String? = null,
)

@Serializable
data class WorkshopSettings(
    val workshopName: String,
    val workshopLogo: String? = null,
    val address: String,
    val phone: String,
    val cnpj: String,
    val reportHeader: String,
    val showLogoInReport: Boolean,
    val requireSignature: Boolean,
)
