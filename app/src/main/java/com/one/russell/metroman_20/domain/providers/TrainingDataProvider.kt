package com.one.russell.metroman_20.domain.providers

import com.one.russell.metroman_20.domain.TrainingData
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

class TrainingDataProvider {
    private val _trainingData: MutableSharedFlow<TrainingData> = MutableSharedFlow(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val trainingData: SharedFlow<TrainingData>
        get() = _trainingData

    suspend fun setTrainingData(trainingData: TrainingData) {
        _trainingData.emit(trainingData)
    }
}