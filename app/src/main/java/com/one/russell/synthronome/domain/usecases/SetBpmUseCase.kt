package com.one.russell.synthronome.domain.usecases

import com.one.russell.synthronome.domain.providers.BpmProvider

class SetBpmUseCase(
    private val bpmProvider: BpmProvider
) {
    fun execute(bpm: Int) {
        bpmProvider.bpmFlow.value = bpm
    }
}