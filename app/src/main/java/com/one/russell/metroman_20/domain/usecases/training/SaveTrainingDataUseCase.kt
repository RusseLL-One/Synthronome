package com.one.russell.metroman_20.domain.usecases.training

import com.one.russell.metroman_20.data.prefs.PreferencesDataStore
import com.one.russell.metroman_20.domain.TrainingData

class SaveTrainingDataUseCase(
    private val dataStore: PreferencesDataStore
) {
    suspend fun execute(trainingData: TrainingData) {
        when (trainingData) {
            is TrainingData.TempoIncreasing.ByBars -> {
                dataStore.training_tempoIncreasing_startBpm.setValue(trainingData.startBpm)
                dataStore.training_tempoIncreasing_endBpm.setValue(trainingData.endBpm)
                dataStore.training_tempoIncreasing_increaseOn.setValue(trainingData.increaseOn)
                dataStore.training_tempoIncreasing_byBars_everyBars.setValue(trainingData.everyBars)
            }
            is TrainingData.TempoIncreasing.ByTime -> {
                dataStore.training_tempoIncreasing_startBpm.setValue(trainingData.startBpm)
                dataStore.training_tempoIncreasing_endBpm.setValue(trainingData.endBpm)
                dataStore.training_tempoIncreasing_increaseOn.setValue(trainingData.increaseOn)
                dataStore.training_tempoIncreasing_byTime_everySeconds.setValue(trainingData.everySeconds)
            }
            is TrainingData.BarDropping.Randomly -> {
                dataStore.training_barDropping_randomly_chancePercent.setValue(trainingData.chancePercent)
            }
            is TrainingData.BarDropping.ByValue -> {
                dataStore.training_barDropping_byValue_ordinaryBarsCount.setValue(trainingData.ordinaryBarsCount)
                dataStore.training_barDropping_byValue_mutedBarsCount.setValue(trainingData.mutedBarsCount)
            }
            is TrainingData.BeatDropping -> {
                dataStore.training_beatDropping_chancePercent.setValue(trainingData.chancePercent)
            }
        }
    }
}