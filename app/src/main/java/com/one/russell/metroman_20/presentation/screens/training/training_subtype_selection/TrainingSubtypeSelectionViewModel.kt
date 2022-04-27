package com.one.russell.metroman_20.presentation.screens.training.training_subtype_selection

import androidx.lifecycle.ViewModel
import com.one.russell.metroman_20.R
import com.one.russell.metroman_20.domain.Colors
import com.one.russell.metroman_20.domain.TrainingFinalType
import com.one.russell.metroman_20.domain.TrainingTopLevelType
import com.one.russell.metroman_20.domain.usecases.colors.ObserveColorsUseCase
import com.one.russell.metroman_20.presentation.screens.training.training_subtype_selection.adapter.TrainingSubtypeItem

class TrainingSubtypeSelectionViewModel(
    private val observeColorsUseCase: ObserveColorsUseCase
) : ViewModel() {

    val colors: Colors get() = observeColorsUseCase.execute().value

    fun getTrainingSubtypeItems(topLevelType: TrainingTopLevelType): List<TrainingSubtypeItem> = when (topLevelType) {
        TrainingTopLevelType.TEMPO_INCREASING -> listOf(
            TrainingSubtypeItem(
                TrainingFinalType.TEMPO_INCREASING_BY_BARS,
                R.string.training_finalType_tempoIncreasing_byBars,
                colors.primaryColor,
                colors.secondaryColor
            ),
            TrainingSubtypeItem(
                TrainingFinalType.TEMPO_INCREASING_BY_TIME,
                R.string.training_finalType_tempoIncreasing_byTime,
                colors.primaryColor,
                colors.secondaryColor
            ),
        )
        TrainingTopLevelType.BAR_DROPPING -> listOf(
            TrainingSubtypeItem(
                TrainingFinalType.BAR_DROPPING_RANDOMLY,
                R.string.training_finalType_barDropping_randomly,
                colors.primaryColor,
                colors.secondaryColor
            ),
            TrainingSubtypeItem(
                TrainingFinalType.BAR_DROPPING_BY_VALUE,
                R.string.training_finalType_barDropping_byValue,
                colors.primaryColor,
                colors.secondaryColor
            ),
        )
        TrainingTopLevelType.BEAT_DROPPING -> throw IllegalArgumentException("Beat dropping type is not allowed here")
    }
}