package br.com.manieri.amanitamuscaria.domain.models

data class CompanySettings(
    val id: Int = 0,
    val companyName: String,
    val address: String,
    val phone: String,
    val whatsapp: String,
    val logoUri: String?,
    val cropX: Float,
    val cropY: Float
)
