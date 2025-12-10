package br.com.manieri.amanitamuscaria.di

import androidx.room.Room
import br.com.manieri.amanitamuscaria.data.local.database.AppDatabase
import br.com.manieri.amanitamuscaria.database.repository.CarroRepository
import br.com.manieri.amanitamuscaria.util.Constants
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataBaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            Constants.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    single { get<AppDatabase>().carroDao() }
    single { get<AppDatabase>().vehicleEntryDao() }

    single { CarroRepository(get<AppDatabase>()) }
}
