package com.one.russell.synthronome.domain.providers

import com.one.russell.synthronome.domain.Colors
import kotlinx.coroutines.flow.MutableStateFlow

class ColorsProvider {
    val colorFlow = MutableStateFlow(Colors())

    fun changeValue(action: Colors.() -> Colors) {
        colorFlow.value = colorFlow.value.action()
    }
}