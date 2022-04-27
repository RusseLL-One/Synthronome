package com.one.russell.metroman_20.presentation.screens.training.training_type_selection

import androidx.lifecycle.ViewModel
import com.one.russell.metroman_20.R
import com.one.russell.metroman_20.domain.TrainingTopLevelType
import com.one.russell.metroman_20.presentation.screens.training.training_type_selection.adapter.TrainingTypeItem

class TrainingTypeSelectionViewModel : ViewModel() {

    fun getTrainingTypeItems(): List<TrainingTypeItem> = listOf(
        TrainingTypeItem(
            TrainingTopLevelType.TEMPO_INCREASING,
            R.string.training_topLevelType_tempoIncreasing
        ),
        TrainingTypeItem(
            TrainingTopLevelType.BAR_DROPPING,
            R.string.training_topLevelType_barDropping
        ),
        TrainingTypeItem(
            TrainingTopLevelType.BEAT_DROPPING,
            R.string.training_topLevelType_beatDropping
        )
    )
}