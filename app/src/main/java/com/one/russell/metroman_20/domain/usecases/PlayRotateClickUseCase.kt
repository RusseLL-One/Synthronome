package com.one.russell.metroman_20.domain.usecases

import com.one.russell.metroman_20.domain.wrappers.Clicker

class PlayRotateClickUseCase(
    private val clicker: Clicker
) {
    fun execute() = clicker.playRotateClick()
}