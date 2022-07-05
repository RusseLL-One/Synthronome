package com.one.russell.synthronome.domain.wrappers

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.core.content.ContextCompat

class Vibrator(context: Context) {
    private var vibrator: Vibrator? = ContextCompat.getSystemService(context, Vibrator::class.java)
    private var isVibrationEnabled: Boolean = false

    fun setEnabled(isEnabled: Boolean) {
        isVibrationEnabled = isEnabled
    }

    @Suppress("DEPRECATION")
    fun performVibrateIfEnabled() {
        if (isVibrationEnabled) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                vibrator?.vibrate(
                    VibrationEffect.createOneShot(
                        VIBRATION_DURATION,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            else vibrator?.vibrate(VIBRATION_DURATION)
        }
    }

    companion object {
        private const val VIBRATION_DURATION = 50L
    }
}