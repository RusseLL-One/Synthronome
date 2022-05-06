package com.one.russell.metroman_20.domain.usecases.training

import com.one.russell.metroman_20.data.prefs.PreferencesDataStore
import com.one.russell.metroman_20.presentation.screens.training.options.OptionsAdjusterType
import com.one.russell.metroman_20.presentation.screens.training.options.OptionsAdjusterType.*

class RestoreTrainingDataUseCase(
    private val dataStore: PreferencesDataStore
) {
    suspend fun execute(type: OptionsAdjusterType): Int {
        return when (type) {
            TEMPO_INCREASING_START_BPM -> dataStore.training_tempoIncreasing_startBpm.getValue()
            TEMPO_INCREASING_END_BPM -> dataStore.training_tempoIncreasing_endBpm.getValue()
            TEMPO_INCREASING_INCREASE_ON -> dataStore.training_tempoIncreasing_increaseOn.getValue()
            TEMPO_INCREASING_BY_BARS_EVERY_BARS -> dataStore.training_tempoIncreasing_byBars_everyBars.getValue()
            TEMPO_INCREASING_BY_TIME_EVERY_SECONDS -> dataStore.training_tempoIncreasing_byTime_everySeconds.getValue()
            BAR_DROPPING_RANDOMLY_CHANCE -> dataStore.training_barDropping_randomly_chancePercent.getValue()
            BAR_DROPPING_BY_VALUE_ORDINARY_BARS_COUNT -> dataStore.training_barDropping_byValue_ordinaryBarsCount.getValue()
            BAR_DROPPING_BY_VALUE_MUTED_BARS_COUNT -> dataStore.training_barDropping_byValue_mutedBarsCount.getValue()
            BEAT_DROPPING_CHANCE -> dataStore.training_beatDropping_chancePercent.getValue()
        }
    }
}