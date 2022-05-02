package com.one.russell.metroman_20.domain.usecases

import com.one.russell.metroman_20.domain.providers.BeatTypesProvider
import com.one.russell.metroman_20.domain.providers.BookmarksProvider
import com.one.russell.metroman_20.domain.providers.BpmProvider

class RemoveBookmarkUseCase(
    private val bookmarksProvider: BookmarksProvider
) {
    fun execute(bookmarkId: String) {
        val currentBookmarks = bookmarksProvider.bookmarksFlow.value
        val bookmarkToRemove = currentBookmarks.find { it.id == bookmarkId } ?: return
        bookmarksProvider.changeValue { this - bookmarkToRemove }
    }
}