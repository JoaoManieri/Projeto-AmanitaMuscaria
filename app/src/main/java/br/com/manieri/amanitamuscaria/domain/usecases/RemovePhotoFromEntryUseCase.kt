package br.com.manieri.amanitamuscaria.domain.usecases

import br.com.manieri.amanitamuscaria.domain.models.VehicleEntry
import br.com.manieri.amanitamuscaria.domain.repository.VehicleEntryRepository

class RemovePhotoFromEntryUseCase(
    private val repository: VehicleEntryRepository
) {
    suspend operator fun invoke(id: String, uri: String): VehicleEntry = repository.removePhoto(id, uri)
}
