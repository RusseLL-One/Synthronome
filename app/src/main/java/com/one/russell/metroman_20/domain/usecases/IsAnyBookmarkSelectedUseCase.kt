package com.one.russell.metroman_20.domain.usecases

import com.one.russell.metroman_20.domain.providers.BookmarksProvider

class IsAnyBookmarkSelectedUseCase(
    private val bookmarksProvider: BookmarksProvider
) {
    fun execute(): Boolean {
        return bookmarksProvider.bookmarksFlow.value.any { it.isSelected }
    }
}