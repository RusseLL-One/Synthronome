package com.one.russell.synthronome.domain

import android.graphics.Color
import androidx.annotation.ColorInt

data class Colors(
    @ColorInt val colorPrimary: Int = Color.BLACK,
    @ColorInt val colorSecondary: Int = Color.BLACK,
    @ColorInt val colorBackground: Int = Color.BLACK,
    @ColorInt val colorOnPrimary: Int = Color.BLACK,
    @ColorInt val colorOnBackground: Int = Color.BLACK,
)