package com.one.russell.synthronome.domain.providers

import com.one.russell.synthronome.domain.BeatType
import kotlinx.coroutines.flow.MutableStateFlow

class BeatTypesProvider {
    val beatTypesFlow = MutableStateFlow(listOf(BeatType.ACCENT))
}