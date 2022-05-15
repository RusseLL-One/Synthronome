package com.one.russell.synthronome.domain.providers

import com.one.russell.synthronome.domain.ClickState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ClickStateProvider {
    private val _clickState = MutableStateFlow(ClickState.IDLE)
    val clickState: StateFlow<ClickState>
        get() = _clickState

    suspend fun setClickState(clickState: ClickState) {
        _clickState.emit(clickState)
    }
}