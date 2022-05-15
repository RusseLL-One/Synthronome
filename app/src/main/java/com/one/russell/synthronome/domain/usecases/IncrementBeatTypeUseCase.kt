package com.one.russell.synthronome.domain.usecases

import com.one.russell.synthronome.domain.BeatType
import com.one.russell.synthronome.domain.providers.BeatTypesProvider

class IncrementBeatTypeUseCase(
    private val beatTypesProvider: BeatTypesProvider
) {
    fun execute(index: Int) {
        val currentBeatTypes = beatTypesProvider.beatTypesFlow.value
        val newBeatTypes = currentBeatTypes.toMutableList()

        currentBeatTypes.getOrNull(index)?.let { beatType ->
            newBeatTypes.set(index, incrementBeatType(beatType))
        }

        beatTypesProvider.beatTypesFlow.value = newBeatTypes
    }

    private fun incrementBeatType(beatType: BeatType): BeatType {
        return when (beatType) {
            BeatType.MUTE -> BeatType.BEAT
            BeatType.BEAT -> BeatType.SUBACCENT
            BeatType.SUBACCENT -> BeatType.ACCENT
            BeatType.ACCENT -> BeatType.MUTE
        }
    }
}