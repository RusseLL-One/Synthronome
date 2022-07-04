package com.one.russell.synthronome.presentation.screens.training.training_subtype_selection

import androidx.lifecycle.ViewModel
import com.one.russell.synthronome.R
import com.one.russell.synthronome.domain.TrainingFinalType
import com.one.russell.synthronome.domain.TrainingTopLevelType
import com.one.russell.synthronome.domain.usecases.colors.ObserveColorsUseCase
import com.one.russell.synthronome.presentation.screens.training.training_subtype_selection.adapter.TrainingSubtypeItem

class TrainingSubtypeSelectionViewModel(
    private val observeColorsUseCase: ObserveColorsUseCase
) : ViewModel() {

    val colors get() = observeColorsUseCase.execute().value

    fun getTrainingSubtypeItems(topLevelType: TrainingTopLevelType): List<TrainingSubtypeItem> = when (topLevelType) {
        TrainingTopLevelType.TEMPO_CHANGE -> listOf(
            TrainingSubtypeItem(
                TrainingFinalType.TEMPO_CHANGE_BY_BARS,
                R.string.training_finalType_tempoChange_byBars,
                colors.colorPrimary,
                colors.colorSecondary,
                colors.colorOnBackground,
            ),
            TrainingSubtypeItem(
                TrainingFinalType.TEMPO_CHANGE_BY_TIME,
                R.string.training_finalType_tempoChange_byTime,
                colors.colorPrimary,
                colors.colorSecondary,
                colors.colorOnBackground
            ),
        )
        TrainingTopLevelType.BAR_DROPPING -> listOf(
            TrainingSubtypeItem(
                TrainingFinalType.BAR_DROPPING_RANDOMLY,
                R.string.training_finalType_barDropping_randomly,
                colors.colorPrimary,
                colors.colorSecondary,
                colors.colorOnBackground
            ),
            TrainingSubtypeItem(
                TrainingFinalType.BAR_DROPPING_BY_VALUE,
                R.string.training_finalType_barDropping_byValue,
                colors.colorPrimary,
                colors.colorSecondary,
                colors.colorOnBackground
            ),
        )
        TrainingTopLevelType.BEAT_DROPPING -> throw IllegalArgumentException("Beat dropping type is not allowed here")
    }
}