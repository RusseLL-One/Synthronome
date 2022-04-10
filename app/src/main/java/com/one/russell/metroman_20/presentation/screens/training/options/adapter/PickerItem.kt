package com.one.russell.metroman_20.presentation.screens.training.options.adapter

import androidx.annotation.StringRes
import com.one.russell.metroman_20.presentation.screens.training.options.OptionsAdjusterType

data class PickerItem(
    val type: OptionsAdjusterType,
    @StringRes val titleRes: Int,
    val initValue: Int,
    val onValueChanged: (Int) -> Unit
): ListItem {
    override val id: String
        get() = type.name
}