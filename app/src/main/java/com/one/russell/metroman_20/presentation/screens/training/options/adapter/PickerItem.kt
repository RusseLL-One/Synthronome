package com.one.russell.metroman_20.presentation.screens.training.options.adapter

import androidx.annotation.StringRes
import java.util.*

data class PickerItem(
    val minValue: Int,
    val maxValue: Int,
    val step: Int,
    @StringRes val titleRes: Int,
    val initValue: Int,
    val onValueChanged: (Int) -> Unit
): ListItem {
    override val id: String
        get() = UUID.randomUUID().toString()
}