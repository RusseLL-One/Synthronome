package com.one.russell.synthronome.presentation.screens.training.training_subtype_selection.adapter

import androidx.annotation.ColorInt
import androidx.annotation.StringRes
import com.one.russell.synthronome.domain.TrainingFinalType

data class TrainingSubtypeItem(
    val type: TrainingFinalType,
    @StringRes val textRes: Int,
    @ColorInt val colorPrimary: Int,
    @ColorInt val colorSecondary: Int,
    @ColorInt val colorOnBackground: Int,
)