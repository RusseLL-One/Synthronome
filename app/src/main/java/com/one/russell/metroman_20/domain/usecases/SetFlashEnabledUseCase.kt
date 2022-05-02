package com.one.russell.metroman_20.domain.usecases

import com.one.russell.metroman_20.domain.providers.OptionsProvider

class SetFlashEnabledUseCase(
    private val optionsProvider: OptionsProvider
) {
    fun execute(isEnabled: Boolean) {
        optionsProvider.changeValue { copy(isFlashEnabled = isEnabled) }
    }
}