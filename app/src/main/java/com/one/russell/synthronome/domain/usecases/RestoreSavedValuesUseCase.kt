package com.one.russell.synthronome.domain.usecases

import com.one.russell.synthronome.data.deserialize
import com.one.russell.synthronome.data.prefs.PreferencesDataStore
import com.one.russell.synthronome.domain.Colors
import com.one.russell.synthronome.domain.Options
import com.one.russell.synthronome.domain.providers.*

class RestoreSavedValuesUseCase(
    private val bpmProvider: BpmProvider,
    private val beatTypesProvider: BeatTypesProvider,
    private val colorsProvider: ColorsProvider,
    private val optionsProvider: OptionsProvider,
    private val bookmarksProvider: BookmarksProvider,
    private val dataStore: PreferencesDataStore
) {
    suspend fun execute() {
        bpmProvider.bpmFlow.emit(dataStore.bpm.getValue())
        beatTypesProvider.beatTypesFlow.emit(dataStore.beatTypes.getValue().deserialize())
        bookmarksProvider.bookmarksFlow.emit(dataStore.bookmarks.getValue().deserialize())

        colorsProvider.colorFlow.emit(
            Colors(
                dataStore.color_primary.getValue(),
                dataStore.color_secondary.getValue(),
                dataStore.color_background.getValue()
            )
        )

        optionsProvider.optionsFlow.emit(
            Options(
                isFlashEnabled = dataStore.isFlashEnabled.getValue(),
                isVibrationEnabled = dataStore.isVibrationEnabled.getValue(),
                soundPresetId = dataStore.soundPresetId.getValue()
            )
        )
    }
}