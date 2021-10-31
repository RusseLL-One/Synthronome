package com.one.russell.metroman_20.presentation.screens.training.options.adapter

import androidx.annotation.StringRes
import com.one.russell.metroman_20.presentation.screens.training.options.OptionsAdjusterType
import kotlinx.coroutines.flow.StateFlow

data class KnobItem(
    val type: OptionsAdjusterType,
    @StringRes val titleRes: Int,
    val valueStateFlow: StateFlow<Int>,
    val onValueChanged: (Int) -> Unit
): ListItem {
    override val id: String
        get() = type.name
}