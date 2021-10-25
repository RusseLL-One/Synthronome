package com.one.russell.metroman_20.presentation.screens.training_type_selection

import androidx.lifecycle.ViewModel
import com.one.russell.metroman_20.domain.TrainingType
import com.one.russell.metroman_20.presentation.screens.training_type_selection.adapter.TrainingTypeItem

class TrainingTypeSelectionViewModel() : ViewModel() {

    fun getTrainingTypeItems(): List<TrainingTypeItem> = listOf(
        TrainingTypeItem(TrainingType.TEMPO_INCREASING, "Увеличение темпа"),
        TrainingTypeItem(TrainingType.BAR_DROPPING, "Глушение такта"),
        TrainingTypeItem(TrainingType.BEAT_DROPPING, "Глушение клика")
    )
}