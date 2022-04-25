package com.one.russell.metroman_20.domain.usecases.colors

import com.one.russell.metroman_20.domain.Colors
import com.one.russell.metroman_20.domain.providers.ColorsProvider
import kotlinx.coroutines.flow.StateFlow

class ObserveColorsUseCase(
    private val colorsProvider: ColorsProvider
) {
    fun execute(): StateFlow<Colors> = colorsProvider.colorFlow
}