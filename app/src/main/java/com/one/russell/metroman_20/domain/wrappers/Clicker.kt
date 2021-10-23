package com.one.russell.metroman_20.domain.wrappers

import android.content.res.AssetManager
import com.one.russell.metroman_20.domain.BeatType

class Clicker(assetManager: AssetManager) {

    private var beatNumber = 0;
    private var callback: ClickerJNICallback = object : ClickerJNICallback {
        override fun onTick() {
            beatNumber++
            if (beatNumber % 4 == 0) {
                native_set_next_beat_type(BeatType.ACCENT)
                beatNumber = 0
            }
        }
    }

    init {
        native_init(callback, assetManager)
    }


    fun start() {
        native_set_next_beat_type(BeatType.ACCENT)
        native_start()
    }

    fun stop() {
        native_stop()
        beatNumber = 0
    }

    fun setBpm(bpm: Int) {
        native_set_bpm(bpm)
    }

    fun playRotateClick() {
        native_play_rotate_click()
    }

    private external fun native_init(listener: ClickerJNICallback, assetManager: AssetManager)
    private external fun native_start()
    private external fun native_stop()
    private external fun native_set_next_beat_type(beatType: BeatType)
    private external fun native_set_bpm(bpm: Int)
    private external fun native_play_rotate_click()
}