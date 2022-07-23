package com.one.russell.synthronome.domain

import android.graphics.Color
import androidx.annotation.ColorInt

data class Colors(
    @ColorInt val colorPrimary: Int = Color.TRANSPARENT,
    @ColorInt val colorSecondary: Int = Color.TRANSPARENT,
    @ColorInt val colorBackground: Int = Color.TRANSPARENT,
    @ColorInt val colorOnPrimary: Int = Color.TRANSPARENT,
    @ColorInt val colorOnBackground: Int = Color.TRANSPARENT,
)