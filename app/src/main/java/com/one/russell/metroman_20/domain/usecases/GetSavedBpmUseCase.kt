package com.one.russell.metroman_20.domain.usecases

import com.one.russell.metroman_20.data.prefs.PreferencesDataStore

class GetSavedBpmUseCase(
    private val preferencesDataStore: PreferencesDataStore
) {
    suspend fun execute(): Int = preferencesDataStore.getBpm()
}