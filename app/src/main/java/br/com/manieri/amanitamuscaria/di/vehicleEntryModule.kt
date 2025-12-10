package br.com.manieri.amanitamuscaria.di

import br.com.manieri.amanitamuscaria.data.repository.VehicleEntryRepositoryImpl
import br.com.manieri.amanitamuscaria.domain.repository.VehicleEntryRepository
import br.com.manieri.amanitamuscaria.domain.usecases.DeleteVehicleEntryUseCase
import br.com.manieri.amanitamuscaria.domain.usecases.GetVehicleEntriesUseCase
import br.com.manieri.amanitamuscaria.domain.usecases.SaveVehicleEntryUseCase
import br.com.manieri.amanitamuscaria.domain.usecases.SearchVehicleEntriesUseCase
import org.koin.dsl.module

val vehicleEntryModule = module {
    single<VehicleEntryRepository> { VehicleEntryRepositoryImpl(get()) }
    single { SaveVehicleEntryUseCase(get()) }
    single { GetVehicleEntriesUseCase(get()) }
    single { DeleteVehicleEntryUseCase(get()) }
    single { SearchVehicleEntriesUseCase(get()) }
}
