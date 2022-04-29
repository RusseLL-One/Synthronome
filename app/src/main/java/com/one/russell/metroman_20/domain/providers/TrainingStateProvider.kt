package com.one.russell.metroman_20.domain.providers

import com.one.russell.metroman_20.domain.TrainingState
import kotlinx.coroutines.flow.MutableStateFlow

class TrainingStateProvider {
    val trainingState = MutableStateFlow<TrainingState>(TrainingState.Idle)
}