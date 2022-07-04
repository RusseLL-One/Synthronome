package com.one.russell.synthronome

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class HostActivity : AppCompatActivity() {

    private val viewModel: HostViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel.restoreValues()

        repeatOnResume {
            viewModel.colors.collect {
                window?.setBackgroundDrawable(ColorDrawable(it.colorBackground))
            }
        }
    }

    override fun onStop() {
        viewModel.saveValues()
        super.onStop()
    }
}