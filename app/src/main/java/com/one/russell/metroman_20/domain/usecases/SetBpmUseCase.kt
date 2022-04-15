package com.one.russell.metroman_20.domain.usecases

import com.one.russell.metroman_20.domain.providers.BpmProvider

class SetBpmUseCase(
    private val bpmProvider: BpmProvider
) {
    fun execute(bpm: Int) {
        bpmProvider.bpmFlow.value = bpm
    }
}