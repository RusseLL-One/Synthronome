package com.one.russell.metroman_20.domain.usecases

import com.one.russell.metroman_20.domain.BeatType
import com.one.russell.metroman_20.domain.providers.BeatTypesProvider

class SetTimeSignatureUseCase(
    private val beatTypesProvider: BeatTypesProvider
) {
    fun execute(timeSignature: Int) {
        val currentBeatTypes = beatTypesProvider.beatTypesFlow.value
        if (timeSignature < currentBeatTypes.size) {
            currentBeatTypes.subList(0, timeSignature)
        } else {
            currentBeatTypes + List(timeSignature - currentBeatTypes.size) { BeatType.BEAT }
        }.also { beatTypesProvider.beatTypesFlow.value = it }
    }
}