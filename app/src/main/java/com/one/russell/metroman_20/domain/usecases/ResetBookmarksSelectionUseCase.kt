package com.one.russell.metroman_20.domain.usecases

import com.one.russell.metroman_20.domain.providers.BookmarksProvider

class ResetBookmarksSelectionUseCase(
    private val bookmarksProvider: BookmarksProvider
) {
    fun execute() {
        bookmarksProvider.changeValue {
            map {
                if (it.isSelected) it.copy(isSelected = false)
                else it
            }
        }
    }
}