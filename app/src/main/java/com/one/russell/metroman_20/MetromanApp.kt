package com.one.russell.metroman_20

import android.app.Application

class MetromanApp : Application() {

    companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}