package com.one.russell.synthronome.domain

sealed class TrainingState {
    sealed class Running: TrainingState() {
        class Limited(val completionPercentage: Int): Running()
        object Endless: Running()
    }
    object Idle: TrainingState()
}