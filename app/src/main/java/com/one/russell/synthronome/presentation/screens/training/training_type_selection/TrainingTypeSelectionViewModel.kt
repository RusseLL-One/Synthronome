package com.one.russell.synthronome.presentation.screens.training.training_type_selection

import androidx.lifecycle.ViewModel
import com.one.russell.synthronome.R
import com.one.russell.synthronome.domain.TrainingTopLevelType
import com.one.russell.synthronome.domain.usecases.colors.ObserveColorsUseCase
import com.one.russell.synthronome.presentation.screens.training.training_type_selection.adapter.TrainingTypeItem

class TrainingTypeSelectionViewModel(
    private val observeColorsUseCase: ObserveColorsUseCase
) : ViewModel() {

    val colors get() = observeColorsUseCase.execute().value

    fun getTrainingTypeItems(): List<TrainingTypeItem> = listOf(
        TrainingTypeItem(
            TrainingTopLevelType.TEMPO_INCREASING,
            R.string.training_topLevelType_tempoIncreasing,
            colors.colorPrimary,
            colors.colorSecondary,
            colors.colorOnBackground
        ),
        TrainingTypeItem(
            TrainingTopLevelType.BAR_DROPPING,
            R.string.training_topLevelType_barDropping,
            colors.colorPrimary,
            colors.colorSecondary,
            colors.colorOnBackground
        ),
        TrainingTypeItem(
            TrainingTopLevelType.BEAT_DROPPING,
            R.string.training_topLevelType_beatDropping,
            colors.colorPrimary,
            colors.colorSecondary,
            colors.colorOnBackground
        )
    )
}