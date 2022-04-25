package com.one.russell.metroman_20.domain.providers

import com.one.russell.metroman_20.domain.BeatType
import kotlinx.coroutines.flow.MutableStateFlow

class BeatTypesProvider {
    val beatTypesFlow = MutableStateFlow(listOf(BeatType.ACCENT))
}