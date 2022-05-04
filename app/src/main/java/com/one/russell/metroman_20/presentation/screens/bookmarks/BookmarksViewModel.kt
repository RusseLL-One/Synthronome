package com.one.russell.metroman_20.presentation.screens.bookmarks

import androidx.lifecycle.ViewModel
import com.one.russell.metroman_20.domain.usecases.ClearAllBookmarksUseCase
import com.one.russell.metroman_20.domain.usecases.ObserveBookmarksUseCase
import com.one.russell.metroman_20.domain.usecases.RemoveBookmarkUseCase
import com.one.russell.metroman_20.domain.usecases.ToggleBookmarkSelectionUseCase
import com.one.russell.metroman_20.domain.usecases.colors.ObserveColorsUseCase

class BookmarksViewModel(
    private val observeColorsUseCase: ObserveColorsUseCase,
    private val observeBookmarksUseCase: ObserveBookmarksUseCase,
    private val removeBookmarkUseCase: RemoveBookmarkUseCase,
    private val toggleBookmarkSelectionUseCase: ToggleBookmarkSelectionUseCase,
    private val clearAllBookmarksUseCase: ClearAllBookmarksUseCase
) : ViewModel() {

    val colors get() = observeColorsUseCase.execute().value
    val bookmarks get() = observeBookmarksUseCase.execute()

    fun onBookmarkClicked(id: String) {
        toggleBookmarkSelectionUseCase.execute(id)
    }

    fun onBookmarkRemoved(id: String) {
        removeBookmarkUseCase.execute(id)
    }

    fun onClearClicked() {
        clearAllBookmarksUseCase.execute()
    }
}