package com.one.russell.synthronome.domain.usecases.training

import com.one.russell.synthronome.domain.TrainingProcessor

class StopTrainingUseCase(
    private val trainingProcessor: TrainingProcessor
) {
    fun execute() = trainingProcessor.stopTraining()
}