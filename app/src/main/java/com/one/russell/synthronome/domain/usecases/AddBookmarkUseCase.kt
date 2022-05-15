package com.one.russell.synthronome.domain.usecases

import com.one.russell.synthronome.domain.Bookmark
import com.one.russell.synthronome.domain.providers.BeatTypesProvider
import com.one.russell.synthronome.domain.providers.BookmarksProvider
import com.one.russell.synthronome.domain.providers.BpmProvider
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
                beatTypesList = beatTypesProvider.beatTypesFlow.value,
                isSelected = true
            )
        }
    }

    private fun generateBookmarkId(): String = UUID.randomUUID().toString()
}