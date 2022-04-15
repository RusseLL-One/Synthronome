package com.one.russell.metroman_20.domain.usecases

import com.one.russell.metroman_20.domain.BeatType
import com.one.russell.metroman_20.domain.providers.BeatTypesProvider
import kotlinx.coroutines.flow.StateFlow

class ObserveBeatTypesUseCase(
    private val beatTypesProvider: BeatTypesProvider,
) {
    fun execute(): StateFlow<List<BeatType>> = beatTypesProvider.beatTypesFlow
}