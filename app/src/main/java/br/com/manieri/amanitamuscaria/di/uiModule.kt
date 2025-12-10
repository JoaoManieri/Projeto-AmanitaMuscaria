package br.com.manieri.amanitamuscaria.di

import br.com.manieri.amanitamuscaria.ui.novaEntrada.NovaEntradaViewModel
import br.com.manieri.amanitamuscaria.ui.historico.HistoricoViewModel
import br.com.manieri.amanitamuscaria.ui.detalhesEntrada.DetalhesEntradaViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { NovaEntradaViewModel(get(), get()) }
    viewModel { HistoricoViewModel(get(), get(), get(), get()) }
    viewModel { DetalhesEntradaViewModel(get(), get(), get(), get(), get()) }
}
