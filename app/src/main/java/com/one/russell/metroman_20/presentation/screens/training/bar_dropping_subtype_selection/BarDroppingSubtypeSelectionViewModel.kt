package com.one.russell.metroman_20.presentation.screens.training.bar_dropping_subtype_selection

import androidx.lifecycle.ViewModel
import com.one.russell.metroman_20.presentation.screens.training.bar_dropping_subtype_selection.adapter.BarDroppingSubtypeItem

class BarDroppingSubtypeSelectionViewModel : ViewModel() {

    fun getBarDroppingSubtypeItems(): List<BarDroppingSubtypeItem> = listOf(
        BarDroppingSubtypeItem(BarDroppingSubtype.RANDOMLY, "Случайно"),
        BarDroppingSubtypeItem(BarDroppingSubtype.BY_VALUE, "По значению")
    )
}