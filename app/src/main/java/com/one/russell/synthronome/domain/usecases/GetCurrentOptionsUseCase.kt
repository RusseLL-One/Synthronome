package com.one.russell.synthronome.domain.usecases

import com.one.russell.synthronome.domain.Options
import com.one.russell.synthronome.domain.providers.OptionsProvider

class GetCurrentOptionsUseCase(
    private val optionsProvider: OptionsProvider
) {
    fun execute(): Options = optionsProvider.optionsFlow.value
}