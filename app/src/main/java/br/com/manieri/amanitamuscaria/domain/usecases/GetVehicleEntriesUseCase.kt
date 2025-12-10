package br.com.manieri.amanitamuscaria.domain.usecases

import br.com.manieri.amanitamuscaria.domain.models.VehicleEntry
import br.com.manieri.amanitamuscaria.domain.repository.VehicleEntryRepository

class GetVehicleEntriesUseCase(
    private val repository: VehicleEntryRepository
) {
    suspend operator fun invoke(): List<VehicleEntry> = repository.getAll()
}
