package com.one.russell.metroman_20.domain.usecases.training

import com.one.russell.metroman_20.domain.TrainingProcessor

class StopTrainingUseCase(
    private val trainingProcessor: TrainingProcessor
) {
    fun execute() = trainingProcessor.stopTraining()
}