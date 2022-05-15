package com.one.russell.synthronome.domain

sealed class TrainingState {
    class Running(val durationMs: Long, val shouldBlockControls: Boolean): TrainingState()
    object Idle: TrainingState()
}