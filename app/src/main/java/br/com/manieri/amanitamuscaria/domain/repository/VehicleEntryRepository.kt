package br.com.manieri.amanitamuscaria.domain.repository

import br.com.manieri.amanitamuscaria.domain.models.VehicleEntry

interface VehicleEntryRepository {
    suspend fun save(entry: VehicleEntry)
    suspend fun getAll(): List<VehicleEntry>
    suspend fun delete(id: String)
}
