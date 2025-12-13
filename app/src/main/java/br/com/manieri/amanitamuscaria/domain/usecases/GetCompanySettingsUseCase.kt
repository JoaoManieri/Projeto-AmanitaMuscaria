package br.com.manieri.amanitamuscaria.domain.usecases

import br.com.manieri.amanitamuscaria.domain.repository.CompanySettingsRepository

class GetCompanySettingsUseCase(
    private val repository: CompanySettingsRepository
 ) {
    suspend operator fun invoke() = repository.get()
}
