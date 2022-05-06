package com.one.russell.synthronome.domain.providers

import com.one.russell.synthronome.domain.Options
import kotlinx.coroutines.flow.MutableStateFlow

class OptionsProvider {
    val optionsFlow = MutableStateFlow(Options())

    fun changeValue(action: Options.() -> Options) {
        optionsFlow.value = optionsFlow.value.action()
    }
}