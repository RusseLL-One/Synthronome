package com.one.russell.metroman_20.presentation.screens.training.tempo_increasing_subtype_selection

import androidx.lifecycle.ViewModel
import com.one.russell.metroman_20.presentation.screens.training.tempo_increasing_subtype_selection.adapter.TempoIncreasingSubtypeItem

class TempoIncreasingSubtypeSelectionViewModel : ViewModel() {

    fun getTempoIncreasingSubtypeItems(): List<TempoIncreasingSubtypeItem> = listOf(
        TempoIncreasingSubtypeItem(TempoIncreasingSubtype.BY_BARS, "По тактам"),
        TempoIncreasingSubtypeItem(TempoIncreasingSubtype.BY_TIME, "По времени")
    )
}