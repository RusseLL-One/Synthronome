package com.one.russell.synthronome.domain.usecases.training

import com.one.russell.synthronome.domain.TrainingState
import com.one.russell.synthronome.domain.providers.TrainingStateProvider
import kotlinx.coroutines.flow.StateFlow

class ObserveTrainingStateUseCase(
    private val trainingStateProvider: TrainingStateProvider
) {
    fun execute(): StateFlow<TrainingState> = trainingStateProvider.trainingState
}