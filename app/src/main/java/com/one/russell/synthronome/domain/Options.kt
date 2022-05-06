package com.one.russell.synthronome.domain

data class Options(
    val isFlashEnabled: Boolean = false,
    val isVibrationEnabled: Boolean = false,
    val soundPresetId: Int = 1
)