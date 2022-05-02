package com.one.russell.metroman_20.domain.usecases

import com.one.russell.metroman_20.domain.providers.OptionsProvider

class SetSoundPresetUseCase(
    private val optionsProvider: OptionsProvider
) {
    fun execute(id: Int) {
        optionsProvider.changeValue { copy(soundPresetId = id) }
    }
}