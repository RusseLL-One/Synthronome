package com.one.russell.metroman_20.domain.usecases

import com.one.russell.metroman_20.domain.ClickState
import com.one.russell.metroman_20.domain.providers.ClickStateProvider
import kotlinx.coroutines.flow.StateFlow

class ObserveClickStateUseCase(
    private val clickStateProvider: ClickStateProvider
) {
    fun execute(): StateFlow<ClickState> = clickStateProvider.clickState
}