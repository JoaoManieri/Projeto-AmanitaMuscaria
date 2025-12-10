package br.com.manieri.amanitamuscaria.di

import br.com.manieri.amanitamuscaria.ui.novaEntrada.NovaEntradaViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { NovaEntradaViewModel(get(), get()) }
}
