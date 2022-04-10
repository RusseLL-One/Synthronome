package com.one.russell.metroman_20.domain.usecases.training

import com.one.russell.metroman_20.domain.TrainingData
import com.one.russell.metroman_20.domain.providers.TrainingDataProvider
import kotlinx.coroutines.flow.SharedFlow

class ObserveTrainingDataUseCase(
    private val trainingDataProvider: TrainingDataProvider,
) {
    fun execute(): SharedFlow<TrainingData> = trainingDataProvider.trainingData
}