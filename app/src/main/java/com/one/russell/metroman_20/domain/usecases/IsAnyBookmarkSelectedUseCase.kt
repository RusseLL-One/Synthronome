package com.one.russell.metroman_20.domain.usecases

import com.one.russell.metroman_20.domain.providers.BookmarksProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class IsAnyBookmarkSelectedUseCase(
    private val bookmarksProvider: BookmarksProvider
) {
    fun execute(): Flow<Boolean> {
        return bookmarksProvider.bookmarksFlow.map { list -> list.any { it.isSelected } }
    }
}