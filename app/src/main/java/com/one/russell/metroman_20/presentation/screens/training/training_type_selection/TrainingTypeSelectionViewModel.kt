package com.one.russell.metroman_20.presentation.screens.training.training_type_selection

import androidx.lifecycle.ViewModel
import com.one.russell.metroman_20.R
import com.one.russell.metroman_20.domain.Colors
import com.one.russell.metroman_20.domain.TrainingTopLevelType
import com.one.russell.metroman_20.domain.usecases.colors.ObserveColorsUseCase
import com.one.russell.metroman_20.presentation.screens.training.training_type_selection.adapter.TrainingTypeItem

class TrainingTypeSelectionViewModel(
    private val observeColorsUseCase: ObserveColorsUseCase
) : ViewModel() {

    val colors: Colors get() = observeColorsUseCase.execute().value

    fun getTrainingTypeItems(): List<TrainingTypeItem> = listOf(
        TrainingTypeItem(
            TrainingTopLevelType.TEMPO_INCREASING,
            R.string.training_topLevelType_tempoIncreasing,
            colors.primaryColor,
            colors.secondaryColor,
        ),
        TrainingTypeItem(
            TrainingTopLevelType.BAR_DROPPING,
            R.string.training_topLevelType_barDropping,
            colors.primaryColor,
            colors.secondaryColor,
        ),
        TrainingTypeItem(
            TrainingTopLevelType.BEAT_DROPPING,
            R.string.training_topLevelType_beatDropping,
            colors.primaryColor,
            colors.secondaryColor,
        )
    )
}