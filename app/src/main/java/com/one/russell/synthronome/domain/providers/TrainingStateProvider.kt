package com.one.russell.synthronome.domain.providers

import com.one.russell.synthronome.domain.TrainingState
import kotlinx.coroutines.flow.MutableStateFlow

class TrainingStateProvider {
    val trainingState = MutableStateFlow<TrainingState>(TrainingState.Idle)
}