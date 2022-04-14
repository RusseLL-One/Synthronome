package com.one.russell.metroman_20.domain.usecases

import com.one.russell.metroman_20.domain.providers.BpmProvider
import kotlinx.coroutines.flow.StateFlow

class ObserveBpmUseCase(
    private val bpmProvider: BpmProvider
) {
    fun execute(): StateFlow<Int> = bpmProvider.bpmFlow
}