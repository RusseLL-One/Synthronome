package com.one.russell.metroman_20.domain.usecases

import com.one.russell.metroman_20.data.prefs.PreferencesDataStore
import com.one.russell.metroman_20.data.serialize
import com.one.russell.metroman_20.domain.providers.BeatTypesProvider
import com.one.russell.metroman_20.domain.providers.BpmProvider
import com.one.russell.metroman_20.domain.providers.ColorsProvider
import com.one.russell.metroman_20.domain.providers.OptionsProvider

class SaveValuesUseCase(
    private val bpmProvider: BpmProvider,
    private val beatTypesProvider: BeatTypesProvider,
    private val colorsProvider: ColorsProvider,
    private val optionsProvider: OptionsProvider,
    private val dataStore: PreferencesDataStore
) {
    suspend fun execute() {
        dataStore.bpm.setValue(bpmProvider.bpmFlow.value)
        dataStore.beatTypes.setValue(beatTypesProvider.beatTypesFlow.value.serialize())

        val colors = colorsProvider.colorFlow.value
        dataStore.color_primary.setValue(colors.primaryColor)
        dataStore.color_secondary.setValue(colors.secondaryColor)
        dataStore.color_background.setValue(colors.backgroundColor)

        val options = optionsProvider.optionsFlow.value
        dataStore.soundPresetId.setValue(options.soundPresetId)
        dataStore.isVibrationEnabled.setValue(options.isVibrationEnabled)
        dataStore.isFlashEnabled.setValue(options.isFlashEnabled)
    }
}