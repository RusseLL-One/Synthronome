package com.one.russell.metroman_20.domain

import androidx.annotation.ColorInt

data class Colors(
    @ColorInt val primaryColor: Int,
    @ColorInt val secondaryColor: Int,
    @ColorInt val backgroundColor: Int
)