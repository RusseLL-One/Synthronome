package com.one.russell.synthronome.domain.usecases

import com.one.russell.synthronome.domain.Constants
import com.one.russell.synthronome.domain.providers.BpmProvider

class CalcBpmByTapIntervalUseCase(
    private val bpmProvider: BpmProvider
) {
    private var prevTouchTime = 0L
    private var prevTouchInterval = 0L
    private var isFirstClick = true

    fun onTapClicked() {
        val interval = System.currentTimeMillis() - prevTouchTime

        when {
            interval < MIN_TOUCH_INTERVAL -> {
                // If the click interval is too small, set its minimum value
                prevTouchInterval = MIN_TOUCH_INTERVAL
            }
            interval > MAX_TOUCH_INTERVAL -> {
                // If the click interval is too long, reset the sequence
                isFirstClick = true
                prevTouchTime = System.currentTimeMillis()
                return
            }
            isFirstClick -> {
                prevTouchInterval = interval
                isFirstClick = false
            }
            else -> {
                prevTouchInterval = (prevTouchInterval + interval) / 2
            }
        }

        val bpm = ((60 * 1000) / prevTouchInterval).toInt()

        bpmProvider.bpmFlow.value = bpm
        prevTouchTime = System.currentTimeMillis()
        return
    }

    companion object {
        private const val MIN_TOUCH_INTERVAL: Long = (60L * 1000L) / Constants.MAX_BPM
        private const val MAX_TOUCH_INTERVAL: Long = (60L * 1000L) / Constants.MIN_BPM
    }
}