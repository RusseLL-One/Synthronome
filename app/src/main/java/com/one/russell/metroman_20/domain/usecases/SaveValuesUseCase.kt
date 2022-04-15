package com.one.russell.metroman_20.domain.usecases

import com.one.russell.metroman_20.data.prefs.PreferencesDataStore
import com.one.russell.metroman_20.data.serialize
import com.one.russell.metroman_20.domain.providers.BeatTypesProvider
import com.one.russell.metroman_20.domain.providers.BpmProvider

class SaveValuesUseCase(
    private val bpmProvider: BpmProvider,
    private val beatTypesProvider: BeatTypesProvider,
    private val dataStore: PreferencesDataStore
) {
    suspend fun execute() {
        dataStore.bpm.setValue(bpmProvider.bpmFlow.value)
        dataStore.beatTypes.setValue(beatTypesProvider.beatTypesFlow.value.serialize())
    }
}