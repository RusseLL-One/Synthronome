package com.one.russell.metroman_20.presentation.views.beat_types

import androidx.annotation.ColorInt
import com.one.russell.metroman_20.domain.BeatType

data class BeatTypeItem(
    val index: Int,
    val beatType: BeatType,
    @ColorInt val colorPrimary: Int,
    @ColorInt val colorSecondary: Int
)

fun BeatType.toListItem(
    index: Int,
    @ColorInt colorPrimary: Int,
    @ColorInt colorSecondary: Int
): BeatTypeItem = BeatTypeItem(index, this, colorPrimary, colorSecondary)