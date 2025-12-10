package br.com.manieri.amanitamuscaria.di

import br.com.manieri.amanitamuscaria.error.ErrorHandler
import br.com.manieri.amanitamuscaria.error.ErrorMapper
import org.koin.dsl.module

val errorModule = module {
    single { ErrorMapper() }
    single { ErrorHandler(get()) }
}
