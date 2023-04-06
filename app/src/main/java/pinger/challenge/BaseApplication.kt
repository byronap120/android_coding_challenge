package pinger.challenge

import android.app.Application
import org.koin.core.context.GlobalContext.startKoin
import pinger.challenge.di.appModule

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            modules(appModule)
        }
    }
}