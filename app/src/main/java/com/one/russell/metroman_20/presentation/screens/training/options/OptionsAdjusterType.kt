package com.one.russell.metroman_20.presentation.screens.training.options

import com.one.russell.metroman_20.domain.Constants

enum class OptionsAdjusterType(
    val minValue: Int,
    val maxValue: Int,
    val step: Int = 1
) {
    TEMPO_INCREASING_START_BPM(Constants.MIN_BPM, Constants.MAX_BPM),
    TEMPO_INCREASING_END_BPM(Constants.MIN_BPM, Constants.MAX_BPM),
    TEMPO_INCREASING_BY_BARS_EVERY_BARS(1, 16),
    TEMPO_INCREASING_BY_BARS_INCREASE_ON(1, Constants.MAX_BPM),
    TEMPO_INCREASING_BY_TIME_MINUTES(1, 30),
    BAR_DROPPING_RANDOMLY_CHANCE(0, 100, 5),
    BAR_DROPPING_BY_VALUE_ORDINARY_BARS_COUNT(1, 16),
    BAR_DROPPING_BY_VALUE_MUTED_BARS_COUNT(1, 16),
    BEAT_DROPPING_CHANCE(0, 100, 5)
}