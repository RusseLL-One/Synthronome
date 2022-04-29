package com.one.russell.metroman_20.domain.usecases.training

import com.one.russell.metroman_20.domain.TrainingState
import com.one.russell.metroman_20.domain.providers.TrainingStateProvider
import kotlinx.coroutines.flow.StateFlow

class ObserveTrainingStateUseCase(
    private val trainingStateProvider: TrainingStateProvider
) {
    fun execute(): StateFlow<TrainingState> = trainingStateProvider.trainingState
}