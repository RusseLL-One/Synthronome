package com.one.russell.metroman_20.domain.usecases

import com.one.russell.metroman_20.data.prefs.PreferencesDataStore
import com.one.russell.metroman_20.domain.providers.BpmProvider

class RestoreSavedValuesUseCase(
    private val bpmProvider: BpmProvider,
    private val dataStore: PreferencesDataStore
) {
    suspend fun execute() {
        bpmProvider.bpmFlow.emit(dataStore.bpm.getValue())
    }
}