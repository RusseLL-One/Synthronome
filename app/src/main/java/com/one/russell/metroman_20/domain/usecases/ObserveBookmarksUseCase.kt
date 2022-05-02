package com.one.russell.metroman_20.domain.usecases

import com.one.russell.metroman_20.domain.providers.BookmarksProvider
import kotlinx.coroutines.flow.asStateFlow

class ObserveBookmarksUseCase(
    private val bookmarksProvider: BookmarksProvider
) {
    fun execute() = bookmarksProvider.bookmarksFlow.asStateFlow()
}