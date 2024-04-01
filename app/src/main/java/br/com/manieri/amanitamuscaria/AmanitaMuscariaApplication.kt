package br.com.manieri.amanitamuscaria

import android.app.Application
import br.com.manieri.amanitamuscaria.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class AmanitaMuscariaApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@AmanitaMuscariaApplication)
            modules(appModule)
        }
    }
}