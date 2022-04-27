package com.one.russell.metroman_20.presentation.screens.training.training_type_selection.adapter

import androidx.annotation.StringRes
import com.one.russell.metroman_20.domain.TrainingTopLevelType

data class TrainingTypeItem(
    val type: TrainingTopLevelType,
    @StringRes val textRes: Int
)