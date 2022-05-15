package com.one.russell.synthronome.domain.usecases

import com.one.russell.synthronome.domain.providers.BookmarksProvider

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