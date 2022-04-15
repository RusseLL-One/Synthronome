package com.one.russell.metroman_20.domain.usecases

import com.one.russell.metroman_20.domain.BeatType
import com.one.russell.metroman_20.domain.providers.BeatTypesProvider

class SetBeatsInBarCountUseCase(
    private val beatTypesProvider: BeatTypesProvider
) {
    fun execute(beatsInBarCount: Int) {
        val currentBeatTypes = beatTypesProvider.beatTypesFlow.value
        if (beatsInBarCount < currentBeatTypes.size) {
            currentBeatTypes.subList(0, beatsInBarCount)
        } else {
            currentBeatTypes + List(beatsInBarCount - currentBeatTypes.size) { BeatType.BEAT }
        }.also { beatTypesProvider.beatTypesFlow.value = it }
    }
}