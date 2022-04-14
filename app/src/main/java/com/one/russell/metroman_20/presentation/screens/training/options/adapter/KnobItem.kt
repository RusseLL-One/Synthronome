package com.one.russell.metroman_20.presentation.screens.training.options.adapter

import androidx.annotation.StringRes
import kotlinx.coroutines.flow.StateFlow
import java.util.*

data class KnobItem(
    @StringRes val titleRes: Int,
    val valueStateFlow: StateFlow<Int>,
    val onValueChanged: (Int) -> Unit
): ListItem {
    override val id: String
        get() = UUID.randomUUID().toString()
}