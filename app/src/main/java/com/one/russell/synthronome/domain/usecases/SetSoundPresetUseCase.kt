package com.one.russell.synthronome.domain.usecases

import com.one.russell.synthronome.domain.providers.OptionsProvider

class SetSoundPresetUseCase(
    private val optionsProvider: OptionsProvider
) {
    fun execute(id: Int) {
        optionsProvider.changeValue { copy(soundPresetId = id) }
    }
}