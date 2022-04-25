package com.one.russell.metroman_20.presentation.views.beat_types

import androidx.annotation.ColorInt
import com.one.russell.metroman_20.domain.BeatType

data class BeatTypeItem(
    val index: Int,
    val beatType: BeatType,
    @ColorInt val primaryColor: Int,
    @ColorInt val secondaryColor: Int
)

fun BeatType.toListItem(
    index: Int,
    @ColorInt primaryColor: Int,
    @ColorInt secondaryColor: Int
): BeatTypeItem = BeatTypeItem(index, this, primaryColor, secondaryColor)