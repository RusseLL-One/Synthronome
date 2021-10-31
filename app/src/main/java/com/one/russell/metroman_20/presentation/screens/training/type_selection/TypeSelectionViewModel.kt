package com.one.russell.metroman_20.presentation.screens.training.type_selection

import androidx.lifecycle.ViewModel
import com.one.russell.metroman_20.presentation.screens.training.type_selection.adapter.TypeItem

class TypeSelectionViewModel : ViewModel() {

    fun getTrainingTypeItems(): List<TypeItem> = listOf(
        TypeItem(Type.TEMPO_INCREASING, "Увеличение темпа"),
        TypeItem(Type.BAR_DROPPING, "Глушение такта"),
        TypeItem(Type.BEAT_DROPPING, "Глушение клика")
    )
}