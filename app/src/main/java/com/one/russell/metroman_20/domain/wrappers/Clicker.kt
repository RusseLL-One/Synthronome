package com.one.russell.metroman_20.domain.wrappers

import android.content.res.AssetManager

class Clicker {

    private external fun native_init(assetManager: AssetManager)
    private external fun native_start()
    private external fun native_stop()
    private external fun native_set_bpm(bpm: Int)
    private external fun native_play_rotate_click()
}