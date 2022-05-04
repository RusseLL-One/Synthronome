package com.one.russell.metroman_20.presentation.screens.bookmarks.adapter

import androidx.annotation.ColorInt
import com.one.russell.metroman_20.domain.Bookmark

data class BookmarkListItem(
    val id: String,
    val bpm: Int,
    val timeSignature: Int,
    val isSelected: Boolean,
    @ColorInt val primaryColor: Int,
    @ColorInt val secondaryColor: Int
)

fun Bookmark.toListItem(
    @ColorInt primaryColor: Int,
    @ColorInt secondaryColor: Int
): BookmarkListItem {
    return BookmarkListItem(
        id,
        bpm,
        beatTypesList.size,
        isSelected,
        primaryColor,
        secondaryColor
    )
}