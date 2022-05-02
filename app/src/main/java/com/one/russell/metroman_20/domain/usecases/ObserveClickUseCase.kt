package com.one.russell.metroman_20.domain.usecases

import com.one.russell.metroman_20.domain.wrappers.Clicker
import kotlinx.coroutines.flow.SharedFlow

class ObserveClickUseCase(
    private val clicker: Clicker
) {
    fun execute(): SharedFlow<Clicker.Click> = clicker.onClicked
}