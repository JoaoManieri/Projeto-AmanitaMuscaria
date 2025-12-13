package br.com.manieri.amanitamuscaria.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "company_settings")
data class CompanySettingsEntity(
    @PrimaryKey val id: Int = 0,
    val companyName: String,
    val address: String,
    val phone: String,
    val whatsapp: String,
    val logoUri: String?,
    val cropX: Float,
    val cropY: Float
)
