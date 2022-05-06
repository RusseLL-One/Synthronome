package com.one.russell.synthronome.domain.usecases

import com.one.russell.synthronome.domain.BeatType
import com.one.russell.synthronome.domain.providers.BeatTypesProvider

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