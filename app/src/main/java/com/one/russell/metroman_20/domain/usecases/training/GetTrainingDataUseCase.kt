package com.one.russell.metroman_20.domain.usecases.training

import com.one.russell.metroman_20.data.prefs.PreferencesDataStore
import com.one.russell.metroman_20.presentation.screens.training.options.OptionsAdjusterType
import com.one.russell.metroman_20.presentation.screens.training.options.OptionsAdjusterType.*

class GetTrainingDataUseCase(
    private val dataStore: PreferencesDataStore
) {
    suspend fun execute(): Map<OptionsAdjusterType, Int> {
        return mapOf(
            TEMPO_INCREASING_START_BPM to dataStore.training_tempoIncreasing_startBpm.getValue(),
            TEMPO_INCREASING_END_BPM to dataStore.training_tempoIncreasing_endBpm.getValue(),
            TEMPO_INCREASING_BY_BARS_EVERY_BARS to dataStore.training_tempoIncreasing_byBars_everyBars.getValue(),
            TEMPO_INCREASING_BY_BARS_INCREASE_ON to dataStore.training_tempoIncreasing_byBars_increaseOn.getValue(),
            TEMPO_INCREASING_BY_TIME_MINUTES to dataStore.training_tempoIncreasing_byTime_minutes.getValue(),
            BAR_DROPPING_RANDOMLY_CHANCE to dataStore.training_barDropping_randomly_chancePercent.getValue(),
            BAR_DROPPING_BY_VALUE_ORDINARY_BARS_COUNT to dataStore.training_barDropping_byValue_ordinaryBarsCount.getValue(),
            BAR_DROPPING_BY_VALUE_MUTED_BARS_COUNT to dataStore.training_barDropping_byValue_mutedBarsCount.getValue(),
            BEAT_DROPPING_CHANCE to dataStore.training_beatDropping_chancePercent.getValue(),
        )
    }
}