package com.one.russell.synthronome.domain.usecases

import com.one.russell.synthronome.domain.providers.OptionsProvider

class SetFlashEnabledUseCase(
    private val optionsProvider: OptionsProvider
) {
    fun execute(isEnabled: Boolean) {
        optionsProvider.changeValue { copy(isFlashEnabled = isEnabled) }
    }
}