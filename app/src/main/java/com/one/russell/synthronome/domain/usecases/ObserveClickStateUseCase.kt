package com.one.russell.synthronome.domain.usecases

import com.one.russell.synthronome.domain.ClickState
import com.one.russell.synthronome.domain.providers.ClickStateProvider
import kotlinx.coroutines.flow.StateFlow

class ObserveClickStateUseCase(
    private val clickStateProvider: ClickStateProvider
) {
    fun execute(): StateFlow<ClickState> = clickStateProvider.clickState
}