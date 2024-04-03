package br.com.manieri.amanitamuscaria.di

import br.com.manieri.amanitamuscaria.image.ImageManager
import org.koin.dsl.module


val fileModule = module {
    single { ImageManager() }
}

