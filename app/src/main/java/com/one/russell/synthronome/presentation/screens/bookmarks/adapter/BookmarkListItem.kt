package com.one.russell.synthronome.presentation.screens.bookmarks.adapter

import androidx.annotation.ColorInt
import com.one.russell.synthronome.domain.Bookmark

data class BookmarkListItem(
    val id: String,
    val bpm: Int,
    val timeSignature: Int,
    val isSelected: Boolean,
    @ColorInt val colorPrimary: Int,
    @ColorInt val colorSecondary: Int,
    @ColorInt val colorOnBackground: Int
)

fun Bookmark.toListItem(
    @ColorInt colorPrimary: Int,
    @ColorInt colorSecondary: Int,
    @ColorInt colorOnBackground: Int
): BookmarkListItem {
    return BookmarkListItem(
        id,
        bpm,
        beatTypesList.size,
        isSelected,
        colorPrimary,
        colorSecondary,
        colorOnBackground
    )
}