package com.one.russell.synthronome.domain.usecases

import com.one.russell.synthronome.domain.Bookmark
import com.one.russell.synthronome.domain.providers.BeatTypesProvider
import com.one.russell.synthronome.domain.providers.BookmarksProvider
import com.one.russell.synthronome.domain.providers.BpmProvider

class ToggleBookmarkSelectionUseCase(
    private val bookmarksProvider: BookmarksProvider,
    private val bpmProvider: BpmProvider,
    private val beatTypesProvider: BeatTypesProvider
) {
    fun execute(bookmarkId: String) {
        var selectedBookmark: Bookmark? = null
        bookmarksProvider.changeValue {
            map {
                when {
                    it.id == bookmarkId -> {
                        it.copy(isSelected = !it.isSelected)
                            .apply { if (isSelected) selectedBookmark = this }
                    }
                    it.isSelected -> it.copy(isSelected = false)
                    else -> it
                }
            }
        }

        selectedBookmark?.let {
            bpmProvider.bpmFlow.value = it.bpm
            beatTypesProvider.beatTypesFlow.value = it.beatTypesList
        }
    }
}