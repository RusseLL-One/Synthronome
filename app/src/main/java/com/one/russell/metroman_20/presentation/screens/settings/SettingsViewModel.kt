package com.one.russell.metroman_20.presentation.screens.settings

import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.one.russell.metroman_20.domain.Options
import com.one.russell.metroman_20.domain.usecases.*
import com.one.russell.metroman_20.domain.usecases.colors.ObserveColorsUseCase
import com.one.russell.metroman_20.domain.usecases.colors.SetColorsUseCase
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val setSoundPresetUseCase: SetSoundPresetUseCase,
    private val observeColorsUseCase: ObserveColorsUseCase,
    private val setColorsUseCase: SetColorsUseCase,
    private val setVibrationEnabledUseCase: SetVibrationEnabledUseCase,
    private val setFlashEnabledUseCase: SetFlashEnabledUseCase,
    private val getCurrentOptionsUseCase: GetCurrentOptionsUseCase,
    private val checkIfFlashAvailableUseCase: CheckIfFlashAvailableUseCase
) : ViewModel() {

    val colors get() = observeColorsUseCase.execute()
    val isFlashAvailable get() = checkIfFlashAvailableUseCase.execute()

    fun onSoundPresetChanged(soundPresetIndex: Int) {
        setSoundPresetUseCase.execute(soundPresetIndex)
    }

    fun setPrimaryColor(@ColorInt color: Int) {
        setColorsUseCase.primary(color)
    }

    fun setBackgroundLightness(@FloatRange(from = 0.0, to = 1.0) lightness: Float) {
        setColorsUseCase.backgroundBrightness(lightness)
    }

    fun setVibrationEnabled(isEnabled: Boolean) {
        viewModelScope.launch {
            setVibrationEnabledUseCase.execute(isEnabled)
        }
    }

    fun setFlashEnabled(isEnabled: Boolean) {
        viewModelScope.launch {
            setFlashEnabledUseCase.execute(isEnabled)
        }
    }

    fun getCurrentOptions(): Options = getCurrentOptionsUseCase.execute()
}