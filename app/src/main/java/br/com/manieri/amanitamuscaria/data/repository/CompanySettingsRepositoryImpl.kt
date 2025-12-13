package br.com.manieri.amanitamuscaria.data.repository

import br.com.manieri.amanitamuscaria.data.local.dao.CompanySettingsDao
import br.com.manieri.amanitamuscaria.data.local.entities.CompanySettingsEntity
import br.com.manieri.amanitamuscaria.domain.models.CompanySettings
import br.com.manieri.amanitamuscaria.domain.repository.CompanySettingsRepository

class CompanySettingsRepositoryImpl(
    private val dao: CompanySettingsDao
) : CompanySettingsRepository {
    override suspend fun get(): CompanySettings? = dao.get()?.toDomain()

    override suspend fun save(settings: CompanySettings) {
        dao.upsert(settings.toEntity())
    }
}

private fun CompanySettings.toEntity() = CompanySettingsEntity(
    id = id,
    companyName = companyName,
    address = address,
    phone = phone,
    whatsapp = whatsapp,
    logoUri = logoUri,
    cropX = cropX,
    cropY = cropY
)

private fun CompanySettingsEntity.toDomain() = CompanySettings(
    id = id,
    companyName = companyName,
    address = address,
    phone = phone,
    whatsapp = whatsapp,
    logoUri = logoUri,
    cropX = cropX,
    cropY = cropY
)
