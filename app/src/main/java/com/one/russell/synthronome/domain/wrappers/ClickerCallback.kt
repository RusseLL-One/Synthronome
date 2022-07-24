package com.one.russell.synthronome.domain.wrappers

import androidx.annotation.Keep
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

class ClickerCallback {

    val onClick: MutableSharedFlow<Int> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    @Keep
    fun onTick(bpm: Int) {
        val emitted = onClick.tryEmit(bpm)
        if (!emitted) throw IllegalStateException()
    }
}