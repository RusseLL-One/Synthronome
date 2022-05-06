package com.one.russell.synthronome.domain.usecases

import com.one.russell.synthronome.domain.providers.BpmProvider
import kotlinx.coroutines.flow.StateFlow

class ObserveBpmUseCase(
    private val bpmProvider: BpmProvider
) {
    fun execute(): StateFlow<Int> = bpmProvider.bpmFlow
}