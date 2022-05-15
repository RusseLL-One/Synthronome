package com.one.russell.synthronome.domain.usecases

import com.one.russell.synthronome.domain.providers.BookmarksProvider

class RemoveBookmarkUseCase(
    private val bookmarksProvider: BookmarksProvider
) {
    fun execute(bookmarkId: String) {
        val currentBookmarks = bookmarksProvider.bookmarksFlow.value
        val bookmarkToRemove = currentBookmarks.find { it.id == bookmarkId } ?: return
        bookmarksProvider.changeValue { this - bookmarkToRemove }
    }

    fun removeSelected() {
        val currentBookmarks = bookmarksProvider.bookmarksFlow.value
        val bookmarkToRemove = currentBookmarks.find { it.isSelected } ?: return
        bookmarksProvider.changeValue { this - bookmarkToRemove }
    }
}