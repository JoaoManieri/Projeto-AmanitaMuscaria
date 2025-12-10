package br.com.manieri.amanitamuscaria.domain.repository

import br.com.manieri.amanitamuscaria.domain.models.VehicleEntry

interface VehicleEntryRepository {
    suspend fun save(entry: VehicleEntry)
    suspend fun getAll(): List<VehicleEntry>
    suspend fun getById(id: String): VehicleEntry?
    suspend fun update(entry: VehicleEntry)
    suspend fun addPhoto(id: String, uri: String): VehicleEntry
    suspend fun removePhoto(id: String, uri: String): VehicleEntry
    suspend fun delete(id: String)
}
