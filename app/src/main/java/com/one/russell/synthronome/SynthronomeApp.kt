package com.one.russell.synthronome

import android.app.Application
import com.one.russell.synthronome.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class SynthronomeApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@SynthronomeApp)
            workManagerFactory()
            modules(
                appModule()
            )
        }
    }

    companion object {
        init {
            System.loadLibrary("native-lib")
        }
    }
}