package com.one.russell.synthronome.domain.providers

import com.one.russell.synthronome.domain.Bookmark
import kotlinx.coroutines.flow.MutableStateFlow

class BookmarksProvider {

    val bookmarksFlow = MutableStateFlow(listOf(Bookmark()))

    fun changeValue(action: List<Bookmark>.() -> List<Bookmark>) {
        bookmarksFlow.value = bookmarksFlow.value.action()
    }
}