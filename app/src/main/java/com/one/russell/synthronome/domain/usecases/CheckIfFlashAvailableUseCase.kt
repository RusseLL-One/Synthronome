package com.one.russell.synthronome.domain.usecases

import com.one.russell.synthronome.domain.wrappers.Flasher

class CheckIfFlashAvailableUseCase(
    private val flasher: Flasher
) {
    fun execute(): Boolean = flasher.isFlashAvailable()
}