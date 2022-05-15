package com.one.russell.synthronome.domain.usecases

import com.one.russell.synthronome.data.prefs.PreferencesDataStore

class SetVibrationEnabledUseCase(
    private val dataStore: PreferencesDataStore
) {
    suspend fun execute(isEnabled: Boolean) {
        dataStore.isVibrationEnabled.setValue(isEnabled)
    }
}