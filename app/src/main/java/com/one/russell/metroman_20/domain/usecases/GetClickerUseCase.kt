package com.one.russell.metroman_20.domain.usecases

import com.one.russell.metroman_20.domain.wrappers.Clicker

class GetClickerUseCase(
    private val clicker: Clicker
) {
    fun execute(): Clicker = clicker
}