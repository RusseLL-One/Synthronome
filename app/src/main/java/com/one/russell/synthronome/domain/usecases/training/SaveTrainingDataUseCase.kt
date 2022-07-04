package com.one.russell.synthronome.domain.usecases.training

import com.one.russell.synthronome.data.prefs.PreferencesDataStore
import com.one.russell.synthronome.domain.TrainingData

class SaveTrainingDataUseCase(
    private val dataStore: PreferencesDataStore
) {
    suspend fun execute(trainingData: TrainingData) {
        when (trainingData) {
            is TrainingData.TempoChange.ByBars -> {
                dataStore.training_tempoChange_startBpm.setValue(trainingData.startBpm)
                dataStore.training_tempoChange_endBpm.setValue(trainingData.endBpm)
                dataStore.training_tempoChange_step.setValue(trainingData.step)
                dataStore.training_tempoChange_byBars_everyBars.setValue(trainingData.everyBars)
            }
            is TrainingData.TempoChange.ByTime -> {
                dataStore.training_tempoChange_startBpm.setValue(trainingData.startBpm)
                dataStore.training_tempoChange_endBpm.setValue(trainingData.endBpm)
                dataStore.training_tempoChange_step.setValue(trainingData.step)
                dataStore.training_tempoChange_byTime_everySeconds.setValue(trainingData.everySeconds)
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