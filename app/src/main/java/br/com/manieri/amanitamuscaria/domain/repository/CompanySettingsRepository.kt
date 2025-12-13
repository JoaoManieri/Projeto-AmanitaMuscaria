package br.com.manieri.amanitamuscaria.domain.repository

import br.com.manieri.amanitamuscaria.domain.models.CompanySettings

interface CompanySettingsRepository {
    suspend fun get(): CompanySettings?
    suspend fun save(settings: CompanySettings)
}
