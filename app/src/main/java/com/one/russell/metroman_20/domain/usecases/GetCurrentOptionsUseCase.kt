package com.one.russell.metroman_20.domain.usecases

import com.one.russell.metroman_20.domain.Options
import com.one.russell.metroman_20.domain.providers.OptionsProvider

class GetCurrentOptionsUseCase(
    private val optionsProvider: OptionsProvider
) {
    fun execute(): Options = optionsProvider.optionsFlow.value
}