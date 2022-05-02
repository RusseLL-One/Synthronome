package com.one.russell.metroman_20.domain.providers

import com.one.russell.metroman_20.domain.Options
import kotlinx.coroutines.flow.MutableStateFlow

class OptionsProvider {
    val optionsFlow = MutableStateFlow(Options())

    fun changeValue(action: Options.() -> Options) {
        optionsFlow.value = optionsFlow.value.action()
    }
}