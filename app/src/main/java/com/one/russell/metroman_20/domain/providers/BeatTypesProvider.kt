package com.one.russell.metroman_20.domain.providers

import com.one.russell.metroman_20.domain.BeatType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class BeatTypesProvider {
    private val _beatTypes = MutableStateFlow(
        listOf( // todo
            BeatType.ACCENT,
            BeatType.BEAT,
            BeatType.BEAT,
            BeatType.BEAT
        )
    )

    val beatTypes: StateFlow<List<BeatType>>
        get() = _beatTypes

    fun setBeatTypes(beatsInBar: List<BeatType>) {
        _beatTypes.tryEmit(beatsInBar)
    }
}