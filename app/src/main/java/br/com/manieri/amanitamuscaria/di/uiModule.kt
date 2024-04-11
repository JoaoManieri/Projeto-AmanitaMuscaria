package br.com.manieri.amanitamuscaria.di

import br.com.manieri.amanitamuscaria.ui.novaEntrada.NovaEntradaViewModel
import br.com.manieri.amanitamuscaria.util.editoresImg.ImageHandler
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { NovaEntradaViewModel(get(), get()) }
    single { ImageHandler(androidContext()) }
}