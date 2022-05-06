package com.one.russell.synthronome.domain.usecases

import com.one.russell.synthronome.domain.wrappers.Clicker

class PlayRotateClickUseCase(
    private val clicker: Clicker
) {
    fun execute() = clicker.playRotateClick()
}