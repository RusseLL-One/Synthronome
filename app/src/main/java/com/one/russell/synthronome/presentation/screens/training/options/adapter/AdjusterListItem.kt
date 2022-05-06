package com.one.russell.synthronome.presentation.screens.training.options.adapter

import androidx.annotation.ColorInt
import com.one.russell.synthronome.presentation.screens.training.options.OptionsAdjusterType

data class AdjusterListItem(
    val type: OptionsAdjusterType,
    val value: Int,
    @ColorInt val colorPrimary: Int,
    @ColorInt val colorSecondary: Int
)