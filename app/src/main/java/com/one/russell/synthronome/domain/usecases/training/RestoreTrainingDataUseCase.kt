package com.one.russell.synthronome.domain.usecases.training

import com.one.russell.synthronome.data.prefs.PreferencesDataStore
import com.one.russell.synthronome.presentation.screens.training.options.OptionsAdjusterType
import com.one.russell.synthronome.presentation.screens.training.options.OptionsAdjusterType.*

class RestoreTrainingDataUseCase(
    private val dataStore: PreferencesDataStore
) {
    suspend fun execute(type: OptionsAdjusterType): Int {
        return when (type) {
            TEMPO_CHANGE_START_BPM -> dataStore.training_tempoChange_startBpm.getValue()
            TEMPO_CHANGE_END_BPM -> dataStore.training_tempoChange_endBpm.getValue()
            TEMPO_CHANGE_STEP -> dataStore.training_tempoChange_step.getValue()
            TEMPO_CHANGE_BY_BARS_EVERY_BARS -> dataStore.training_tempoChange_byBars_everyBars.getValue()
            TEMPO_CHANGE_BY_TIME_EVERY_SECONDS -> dataStore.training_tempoChange_byTime_everySeconds.getValue()
            BAR_DROPPING_RANDOMLY_CHANCE -> dataStore.training_barDropping_randomly_chancePercent.getValue()
            BAR_DROPPING_BY_VALUE_ORDINARY_BARS_COUNT -> dataStore.training_barDropping_byValue_ordinaryBarsCount.getValue()
            BAR_DROPPING_BY_VALUE_MUTED_BARS_COUNT -> dataStore.training_barDropping_byValue_mutedBarsCount.getValue()
            BEAT_DROPPING_CHANCE -> dataStore.training_beatDropping_chancePercent.getValue()
        }
    }
}