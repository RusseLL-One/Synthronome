package com.one.russell.synthronome.domain.usecases.training

import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.one.russell.synthronome.domain.TrainingData
import com.one.russell.synthronome.domain.TrainingProcessor
import com.one.russell.synthronome.domain.usecases.StartClickingUseCase
import kotlinx.coroutines.launch

class StartTrainingUseCase(
    private val startClickingUseCase: StartClickingUseCase,
    private val trainingProcessor: TrainingProcessor
) {
    fun execute(trainingData: TrainingData) {
        ProcessLifecycleOwner.get().lifecycleScope.launch {
            startClickingUseCase.execute(trainingData.getStartBpm())
            trainingProcessor.startTraining(trainingData)
        }
    }

    private fun TrainingData.getStartBpm(): Int? = when (this) {
        is TrainingData.TempoChange -> startBpm
        else -> null
    }
}