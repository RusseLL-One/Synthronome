package com.one.russell.synthronome.domain.usecases

import com.one.russell.synthronome.domain.BeatType
import com.one.russell.synthronome.domain.providers.BeatTypesProvider
import kotlinx.coroutines.flow.StateFlow

class ObserveBeatTypesUseCase(
    private val beatTypesProvider: BeatTypesProvider,
) {
    fun execute(): StateFlow<List<BeatType>> = beatTypesProvider.beatTypesFlow
}