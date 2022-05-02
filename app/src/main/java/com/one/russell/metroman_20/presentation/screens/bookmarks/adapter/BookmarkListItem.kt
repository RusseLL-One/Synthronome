package com.one.russell.metroman_20.presentation.screens.bookmarks.adapter

import androidx.annotation.ColorInt

data class BookmarkListItem(
    val id: String,
    val bpm: Int,
    val timeSignature: Int,
    @ColorInt val primaryColor: Int,
    @ColorInt val secondaryColor: Int
)