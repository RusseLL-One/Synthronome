package com.one.russell.synthronome.domain.usecases.colors

import com.one.russell.synthronome.domain.Colors
import com.one.russell.synthronome.domain.providers.ColorsProvider
import kotlinx.coroutines.flow.StateFlow

class ObserveColorsUseCase(
    private val colorsProvider: ColorsProvider
) {
    fun execute(): StateFlow<Colors> = colorsProvider.colorFlow
}