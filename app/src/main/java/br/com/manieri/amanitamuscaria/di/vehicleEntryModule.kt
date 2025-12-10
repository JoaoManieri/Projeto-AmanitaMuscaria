package br.com.manieri.amanitamuscaria.di

import br.com.manieri.amanitamuscaria.data.repository.VehicleEntryRepositoryImpl
import br.com.manieri.amanitamuscaria.domain.repository.VehicleEntryRepository
import br.com.manieri.amanitamuscaria.domain.usecases.DeleteVehicleEntryUseCase
import br.com.manieri.amanitamuscaria.domain.usecases.GetVehicleEntryByIdUseCase
import br.com.manieri.amanitamuscaria.domain.usecases.GetVehicleEntriesUseCase
import br.com.manieri.amanitamuscaria.domain.usecases.AddPhotoToEntryUseCase
import br.com.manieri.amanitamuscaria.domain.usecases.RemovePhotoFromEntryUseCase
import br.com.manieri.amanitamuscaria.domain.usecases.SaveVehicleEntryUseCase
import br.com.manieri.amanitamuscaria.domain.usecases.SearchVehicleEntriesUseCase
import br.com.manieri.amanitamuscaria.domain.usecases.UpdateVehicleEntryUseCase
import org.koin.dsl.module

val vehicleEntryModule = module {
    single<VehicleEntryRepository> { VehicleEntryRepositoryImpl(get()) }
    single { SaveVehicleEntryUseCase(get()) }
    single { GetVehicleEntriesUseCase(get()) }
    single { GetVehicleEntryByIdUseCase(get()) }
    single { UpdateVehicleEntryUseCase(get()) }
    single { AddPhotoToEntryUseCase(get()) }
    single { RemovePhotoFromEntryUseCase(get()) }
    single { DeleteVehicleEntryUseCase(get()) }
    single { SearchVehicleEntriesUseCase(get()) }
}
