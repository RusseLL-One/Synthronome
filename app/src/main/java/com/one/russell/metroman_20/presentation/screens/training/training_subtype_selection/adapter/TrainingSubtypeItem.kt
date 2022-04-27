package com.one.russell.metroman_20.presentation.screens.training.training_subtype_selection.adapter

import androidx.annotation.StringRes
import com.one.russell.metroman_20.domain.TrainingFinalType

data class TrainingSubtypeItem(
    val type: TrainingFinalType,
    @StringRes val textRes: Int
)