package com.one.russell.metroman_20.domain

data class Options(
    val isFlashEnabled: Boolean = false,
    val isVibrationEnabled: Boolean = false,
    val soundPresetId: Int = 1
)