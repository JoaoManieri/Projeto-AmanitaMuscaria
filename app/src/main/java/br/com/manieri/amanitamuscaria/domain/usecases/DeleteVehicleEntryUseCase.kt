package br.com.manieri.amanitamuscaria.domain.usecases

import br.com.manieri.amanitamuscaria.domain.repository.VehicleEntryRepository

class DeleteVehicleEntryUseCase(
    private val repository: VehicleEntryRepository
) {
    suspend operator fun invoke(id: String) = repository.delete(id)
}
