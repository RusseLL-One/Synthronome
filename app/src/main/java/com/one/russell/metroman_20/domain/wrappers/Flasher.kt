package com.one.russell.metroman_20.domain.wrappers

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import androidx.core.content.ContextCompat
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class Flasher(private val context: Context) {
    private var cameraManager: CameraManager? = ContextCompat.getSystemService(context, CameraManager::class.java)
    private var isFlashEnabled: Boolean = false

    fun setEnabled(isEnabled: Boolean) {
        isFlashEnabled = isEnabled
    }

    fun isFlashAvailable(): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)
    }

    suspend fun performFlashIfEnabled() {
        coroutineScope {
            launch {
                if (isFlashEnabled) {
                    setTorchEnabled(true)
                    delay(FLASH_DURATION)
                    setTorchEnabled(false)
                }
            }
        }
    }

    private fun setTorchEnabled(isEnabled: Boolean) {
        cameraManager?.cameraIdList
            ?.getOrNull(0)
            ?.let { cameraManager?.setTorchMode(it, isEnabled) }
    }

    companion object {
        private const val FLASH_DURATION = 100L
    }
}