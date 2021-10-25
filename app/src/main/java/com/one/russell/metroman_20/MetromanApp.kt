package com.one.russell.metroman_20

import android.app.Application
import com.one.russell.metroman_20.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class MetromanApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MetromanApp)
            workManagerFactory()
            modules(
                appModule(applicationContext)
            )
        }
    }

    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }
}