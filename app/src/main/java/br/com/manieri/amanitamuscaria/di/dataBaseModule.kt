package br.com.manieri.amanitamuscaria.di

import androidx.room.Room
import br.com.manieri.amanitamuscaria.database.AppDatabase
import br.com.manieri.amanitamuscaria.database.repository.CarroRepository
import br.com.manieri.amanitamuscaria.util.Constants
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataBaseModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            Constants.DATABASE_NAME)
            .build()
    }

    single { get<AppDatabase>().carroDao() }

    single { CarroRepository(get<AppDatabase>()) }
}