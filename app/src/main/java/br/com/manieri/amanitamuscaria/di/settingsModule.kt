package br.com.manieri.amanitamuscaria.di

import br.com.manieri.amanitamuscaria.data.repository.CompanySettingsRepositoryImpl
import br.com.manieri.amanitamuscaria.domain.repository.CompanySettingsRepository
import br.com.manieri.amanitamuscaria.domain.usecases.GetCompanySettingsUseCase
import br.com.manieri.amanitamuscaria.domain.usecases.SaveCompanySettingsUseCase
import br.com.manieri.amanitamuscaria.ui.configuracoes.ConfiguracoesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    single<CompanySettingsRepository> { CompanySettingsRepositoryImpl(get()) }
    single { GetCompanySettingsUseCase(get()) }
    single { SaveCompanySettingsUseCase(get()) }

    viewModel { ConfiguracoesViewModel(get(), get(), get()) }
}
