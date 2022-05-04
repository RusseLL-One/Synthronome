package com.one.russell.metroman_20.domain.providers

import com.one.russell.metroman_20.domain.Colors
import kotlinx.coroutines.flow.MutableStateFlow

class ColorsProvider {
    val colorFlow = MutableStateFlow(Colors())

    fun changeValue(action: Colors.() -> Colors) {
        colorFlow.value = colorFlow.value.action()
    }
}