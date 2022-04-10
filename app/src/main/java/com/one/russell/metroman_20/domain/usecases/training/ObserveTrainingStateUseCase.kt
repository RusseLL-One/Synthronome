package com.one.russell.metroman_20.domain.usecases.training

import com.one.russell.metroman_20.domain.TrainingProcessor
import com.one.russell.metroman_20.domain.TrainingState
import kotlinx.coroutines.flow.StateFlow

class ObserveTrainingStateUseCase(
    private val trainingProcessor: TrainingProcessor
) {
    fun execute(): StateFlow<TrainingState> = trainingProcessor.trainingState
}