package com.one.russell.synthronome.domain.usecases

import com.one.russell.synthronome.domain.providers.BookmarksProvider

class ClearAllBookmarksUseCase(
    private val bookmarksProvider: BookmarksProvider
) {
    fun execute() {
        bookmarksProvider.changeValue { emptyList() }
    }
}