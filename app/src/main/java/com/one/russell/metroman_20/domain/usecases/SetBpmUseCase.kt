package com.one.russell.metroman_20.domain.usecases

import com.one.russell.metroman_20.data.prefs.PreferencesDataStore
import com.one.russell.metroman_20.domain.providers.BpmProvider
import kotlinx.coroutines.flow.*

class SetBpmUseCase(
    private val bpmProvider: BpmProvider,
    private val dataStore: PreferencesDataStore
) {
    suspend fun execute(bpm: Int) {
        bpmProvider.bpmFlow.emit(bpm)
        bpmProvider.bpmFlow
            .debounce(timeoutMillis = BPM_DEBOUNCE_TIME_MILLIS)
            .collect { dataStore.bpm.setValue(it) }
    }

    companion object {
        private const val BPM_DEBOUNCE_TIME_MILLIS = 1000L
    }
}