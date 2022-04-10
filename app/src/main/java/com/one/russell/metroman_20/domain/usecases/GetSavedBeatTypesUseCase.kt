package com.one.russell.metroman_20.domain.usecases

import com.one.russell.metroman_20.data.prefs.PreferencesDataStore

class GetSavedBeatTypesUseCase(
    private val dataStore: PreferencesDataStore
) {
    suspend fun execute(): Int = dataStore.bpm.getValue()
}