package com.one.russell.metroman_20.domain.providers

import com.one.russell.metroman_20.domain.Bookmark
import kotlinx.coroutines.flow.MutableStateFlow

class BookmarksProvider {

    val bookmarksFlow = MutableStateFlow(listOf(Bookmark()))

    fun changeValue(action: List<Bookmark>.() -> List<Bookmark>) {
        bookmarksFlow.value = bookmarksFlow.value.action()
    }
}