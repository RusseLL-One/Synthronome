package com.one.russell.synthronome.domain.usecases

import com.one.russell.synthronome.domain.wrappers.Clicker
import kotlinx.coroutines.flow.SharedFlow

class ObserveClickUseCase(
    private val clicker: Clicker
) {
    fun execute(): SharedFlow<Clicker.Click> = clicker.onClicked
}