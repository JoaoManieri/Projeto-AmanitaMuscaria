package br.com.manieri.amanitamuscaria.di

import br.com.manieri.amanitamuscaria.data.repository.VehicleEntryRepositoryImpl
import br.com.manieri.amanitamuscaria.domain.repository.VehicleEntryRepository
import br.com.manieri.amanitamuscaria.domain.usecases.SaveVehicleEntryUseCase
import org.koin.dsl.module

val vehicleEntryModule = module {
    single<VehicleEntryRepository> { VehicleEntryRepositoryImpl(get()) }
    single { SaveVehicleEntryUseCase(get()) }
}
