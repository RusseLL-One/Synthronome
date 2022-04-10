package com.one.russell.metroman_20.domain.usecases

import com.one.russell.metroman_20.domain.wrappers.Clicker

class SetBpmUseCase(
    private val clicker: Clicker
) {
    fun execute(bpm: Int) = clicker.setBpm(bpm)
}