package com.one.russell.synthronome.domain.usecases

import com.one.russell.synthronome.domain.providers.BookmarksProvider
import kotlinx.coroutines.flow.asStateFlow

class ObserveBookmarksUseCase(
    private val bookmarksProvider: BookmarksProvider
) {
    fun execute() = bookmarksProvider.bookmarksFlow.asStateFlow()
}