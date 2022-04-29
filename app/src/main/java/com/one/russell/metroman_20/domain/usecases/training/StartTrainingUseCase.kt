package com.one.russell.metroman_20.domain.usecases.training

import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.one.russell.metroman_20.domain.TrainingData
import com.one.russell.metroman_20.domain.TrainingProcessor
import com.one.russell.metroman_20.domain.usecases.StartClickingUseCase
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
        is TrainingData.TempoIncreasing -> startBpm
        else -> null
    }
}