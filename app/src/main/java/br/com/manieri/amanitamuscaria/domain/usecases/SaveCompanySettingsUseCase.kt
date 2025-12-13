package br.com.manieri.amanitamuscaria.domain.usecases

import br.com.manieri.amanitamuscaria.domain.models.CompanySettings
import br.com.manieri.amanitamuscaria.domain.repository.CompanySettingsRepository

class SaveCompanySettingsUseCase(
    private val repository: CompanySettingsRepository
) {
    suspend operator fun invoke(settings: CompanySettings) = repository.save(settings)
}
