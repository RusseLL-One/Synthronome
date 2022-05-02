package com.one.russell.metroman_20.domain.usecases

import com.one.russell.metroman_20.domain.providers.BookmarksProvider

class ToggleBookmarkSelectionUseCase(
    private val bookmarksProvider: BookmarksProvider
) {
    fun execute(bookmarkId: String) {
        bookmarksProvider.changeValue {
            map {
                when {
                    it.id == bookmarkId -> it.copy(isSelected = !it.isSelected)
                    it.isSelected -> it.copy(isSelected = false)
                    else -> it
                }
            }
        }
    }
}