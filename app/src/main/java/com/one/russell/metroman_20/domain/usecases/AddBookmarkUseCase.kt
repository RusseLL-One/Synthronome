package com.one.russell.metroman_20.domain.usecases

import com.one.russell.metroman_20.domain.Bookmark
import com.one.russell.metroman_20.domain.providers.BeatTypesProvider
import com.one.russell.metroman_20.domain.providers.BookmarksProvider
import com.one.russell.metroman_20.domain.providers.BpmProvider
import java.util.*

class AddBookmarkUseCase(
    private val bpmProvider: BpmProvider,
    private val beatTypesProvider: BeatTypesProvider,
    private val bookmarksProvider: BookmarksProvider
) {
    fun execute() {
        bookmarksProvider.changeValue {
            this + Bookmark(
                id = generateBookmarkId(),
                bpm = bpmProvider.bpmFlow.value,
                beatTypesList = beatTypesProvider.beatTypesFlow.value
            )
        }
    }

    private fun generateBookmarkId(): String = UUID.randomUUID().toString()
}