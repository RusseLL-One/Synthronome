package com.one.russell.metroman_20.domain.usecases.training

import com.one.russell.metroman_20.domain.ClickState
import com.one.russell.metroman_20.domain.TrainingData
import com.one.russell.metroman_20.domain.TrainingProcessor
import com.one.russell.metroman_20.domain.providers.ClickStateProvider
import com.one.russell.metroman_20.domain.usecases.StartClickingUseCase
import kotlinx.coroutines.flow.MutableSharedFlow

class StartTrainingUseCase(
    private val startClickingUseCase: StartClickingUseCase,
    private val clickStateProvider: ClickStateProvider,
    private val trainingProcessor: TrainingProcessor
) {
    suspend fun execute(
        trainingData: TrainingData,
        bpmFlow: MutableSharedFlow<Int>
    ) {
        if (clickStateProvider.clickState.value != ClickState.STARTED) {
            startClickingUseCase.execute()
        }
        trainingProcessor.startTraining(trainingData, bpmFlow)
    }
}