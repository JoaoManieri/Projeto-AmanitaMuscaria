package br.com.manieri.amanitamuscaria.domain.usecases

import br.com.manieri.amanitamuscaria.domain.models.VehicleEntry
import br.com.manieri.amanitamuscaria.domain.repository.VehicleEntryRepository

class UpdateVehicleEntryUseCase(
    private val repository: VehicleEntryRepository
) {
    suspend operator fun invoke(entry: VehicleEntry) = repository.update(entry)
}
