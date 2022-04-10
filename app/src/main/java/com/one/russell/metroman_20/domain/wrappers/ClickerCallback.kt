package com.one.russell.metroman_20.domain.wrappers

import androidx.annotation.Keep
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

class ClickerCallback {

    val onClick: MutableSharedFlow<Int> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    @Keep
    @DelicateCoroutinesApi
    fun onTick(bpm: Int) {
        val emitted = onClick.tryEmit(bpm)
        if (!emitted) throw IllegalStateException()
    }
}