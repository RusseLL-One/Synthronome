package com.one.russell.metroman_20.domain

sealed class TrainingState {
    class Running(val durationMs: Long, val shouldBlockControls: Boolean): TrainingState()
    object Idle: TrainingState()
}