package com.one.russell.metroman_20.domain.usecases

import com.one.russell.metroman_20.data.prefs.PreferencesDataStore

class SetVibrationEnabledUseCase(
    private val dataStore: PreferencesDataStore
) {
    suspend fun execute(isEnabled: Boolean) {
        dataStore.isVibrationEnabled.setValue(isEnabled)
    }
}