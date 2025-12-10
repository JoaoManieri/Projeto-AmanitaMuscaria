package br.com.manieri.amanitamuscaria.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "vehicle_entries")
data class VehicleEntryEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val plate: String,
    val brand: String,
    val model: String,
    val year: Int,
    val color: String,
    val mileage: Int,
    val customerName: String,
    val notes: String,
    val createdAt: Long,
    val photos: List<String>
)
