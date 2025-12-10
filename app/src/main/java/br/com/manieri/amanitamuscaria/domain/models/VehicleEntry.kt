package br.com.manieri.amanitamuscaria.domain.models

import java.time.Instant
import java.util.UUID

data class VehicleEntry(
    val id: String = UUID.randomUUID().toString(),
    val plate: String,
    val brand: String,
    val model: String,
    val year: Int,
    val color: String,
    val mileage: Int,
    val customerName: String,
    val notes: String,
    val createdAt: Instant = Instant.now(),
    val photos: List<String> = emptyList()
)
