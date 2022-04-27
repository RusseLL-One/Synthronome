package com.one.russell.metroman_20.presentation.screens.training.training_subtype_selection

import androidx.lifecycle.ViewModel
import com.one.russell.metroman_20.R
import com.one.russell.metroman_20.domain.TrainingFinalType
import com.one.russell.metroman_20.domain.TrainingTopLevelType
import com.one.russell.metroman_20.presentation.screens.training.training_subtype_selection.adapter.TrainingSubtypeItem

class TrainingSubtypeSelectionViewModel : ViewModel() {

    fun getTrainingSubtypeItems(topLevelType: TrainingTopLevelType): List<TrainingSubtypeItem> = when (topLevelType) {
        TrainingTopLevelType.TEMPO_INCREASING -> listOf(
            TrainingSubtypeItem(TrainingFinalType.TEMPO_INCREASING_BY_BARS, R.string.training_finalType_tempoIncreasing_byBars),
            TrainingSubtypeItem(TrainingFinalType.TEMPO_INCREASING_BY_TIME, R.string.training_finalType_tempoIncreasing_byTime),
        )
        TrainingTopLevelType.BAR_DROPPING -> listOf(
            TrainingSubtypeItem(TrainingFinalType.BAR_DROPPING_RANDOMLY, R.string.training_finalType_barDropping_randomly),
            TrainingSubtypeItem(TrainingFinalType.BAR_DROPPING_BY_VALUE, R.string.training_finalType_barDropping_byValue),
        )
        TrainingTopLevelType.BEAT_DROPPING -> throw IllegalArgumentException("Beat dropping type is not allowed here")
    }
}