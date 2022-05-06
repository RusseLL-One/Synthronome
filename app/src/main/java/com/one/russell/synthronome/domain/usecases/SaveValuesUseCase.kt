package com.one.russell.synthronome.domain.usecases

import com.one.russell.synthronome.data.prefs.PreferencesDataStore
import com.one.russell.synthronome.data.serialize
import com.one.russell.synthronome.domain.providers.*

class SaveValuesUseCase(
    private val bpmProvider: BpmProvider,
    private val beatTypesProvider: BeatTypesProvider,
    private val colorsProvider: ColorsProvider,
    private val optionsProvider: OptionsProvider,
    private val bookmarksProvider: BookmarksProvider,
    private val dataStore: PreferencesDataStore
) {
    suspend fun execute() {
        val bpm = bpmProvider.bpmFlow.value
        dataStore.bpm.setValue(bpm)

        val beatTypes = beatTypesProvider.beatTypesFlow.value
        dataStore.beatTypes.setValue(beatTypes.serialize())

        val bookmarks = bookmarksProvider.bookmarksFlow.value
        dataStore.bookmarks.setValue(bookmarks.serialize())

        val colors = colorsProvider.colorFlow.value
        dataStore.color_primary.setValue(colors.colorPrimary)
        dataStore.color_secondary.setValue(colors.colorSecondary)
        dataStore.color_background.setValue(colors.colorBackground)
        dataStore.color_on_primary.setValue(colors.colorOnPrimary)
        dataStore.color_on_background.setValue(colors.colorOnBackground)

        val options = optionsProvider.optionsFlow.value
        dataStore.soundPresetId.setValue(options.soundPresetId)
        dataStore.isVibrationEnabled.setValue(options.isVibrationEnabled)
        dataStore.isFlashEnabled.setValue(options.isFlashEnabled)
    }
}