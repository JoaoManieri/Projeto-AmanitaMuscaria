package br.com.manieri.amanitamuscaria.data.repository

import br.com.manieri.amanitamuscaria.data.local.dao.VehicleEntryDao
import br.com.manieri.amanitamuscaria.data.local.entities.VehicleEntryEntity
import br.com.manieri.amanitamuscaria.domain.models.VehicleEntry
import br.com.manieri.amanitamuscaria.domain.repository.VehicleEntryRepository
import java.time.Instant

class VehicleEntryRepositoryImpl(
    private val vehicleEntryDao: VehicleEntryDao
) : VehicleEntryRepository {

    override suspend fun save(entry: VehicleEntry) {
        vehicleEntryDao.insert(entry.toEntity())
    }

    override suspend fun getAll(): List<VehicleEntry> = vehicleEntryDao.getAll().map { it.toDomain() }

    override suspend fun getById(id: String): VehicleEntry? = vehicleEntryDao.getById(id)?.toDomain()

    override suspend fun update(entry: VehicleEntry) {
        vehicleEntryDao.update(entry.toEntity())
    }

    override suspend fun addPhoto(id: String, uri: String): VehicleEntry {
        val current = vehicleEntryDao.getById(id)?.toDomain() ?: throw IllegalArgumentException("Entrada não encontrada")
        val updated = current.copy(photos = (current.photos + uri).distinct())
        vehicleEntryDao.update(updated.toEntity())
        return updated
    }

    override suspend fun removePhoto(id: String, uri: String): VehicleEntry {
        val current = vehicleEntryDao.getById(id)?.toDomain() ?: throw IllegalArgumentException("Entrada não encontrada")
        val updated = current.copy(photos = current.photos.filterNot { it == uri })
        vehicleEntryDao.update(updated.toEntity())
        return updated
    }

    override suspend fun delete(id: String) {
        vehicleEntryDao.deleteById(id)
    }
}

private fun VehicleEntry.toEntity() = VehicleEntryEntity(
    id = id,
    plate = plate,
    brand = brand,
    model = model,
    year = year,
    color = color,
    mileage = mileage,
    customerName = customerName,
    notes = notes,
    createdAt = createdAt.toEpochMilli(),
    photos = photos
)

private fun VehicleEntryEntity.toDomain() = VehicleEntry(
    id = id,
    plate = plate,
    brand = brand,
    model = model,
    year = year,
    color = color,
    mileage = mileage,
    customerName = customerName,
    notes = notes,
    createdAt = Instant.ofEpochMilli(createdAt),
    photos = photos
)
