package com.one.russell.metroman_20.domain.usecases

import com.one.russell.metroman_20.domain.wrappers.ClickerCallback

class GetClickerCallbackUseCase(
    private val clickerCallback: ClickerCallback
) {
    fun execute(): ClickerCallback = clickerCallback
}