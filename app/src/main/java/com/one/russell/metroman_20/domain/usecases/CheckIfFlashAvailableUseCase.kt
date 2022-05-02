package com.one.russell.metroman_20.domain.usecases

import com.one.russell.metroman_20.domain.wrappers.Flasher

class CheckIfFlashAvailableUseCase(
    private val flasher: Flasher
) {
    fun execute(): Boolean = flasher.isFlashAvailable()
}