package br.com.manieri.amanitamuscaria.domain.usecases

import br.com.manieri.amanitamuscaria.domain.models.VehicleEntry
import br.com.manieri.amanitamuscaria.domain.repository.VehicleEntryRepository

class AddPhotoToEntryUseCase(
    private val repository: VehicleEntryRepository
) {
    suspend operator fun invoke(id: String, uri: String): VehicleEntry = repository.addPhoto(id, uri)
}
