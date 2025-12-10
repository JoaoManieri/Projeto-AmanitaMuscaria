package br.com.manieri.amanitamuscaria.domain.usecases

import br.com.manieri.amanitamuscaria.domain.models.VehicleEntry
import br.com.manieri.amanitamuscaria.domain.repository.VehicleEntryRepository

class GetVehicleEntryByIdUseCase(
    private val repository: VehicleEntryRepository
) {
    suspend operator fun invoke(id: String): VehicleEntry? = repository.getById(id)
}
