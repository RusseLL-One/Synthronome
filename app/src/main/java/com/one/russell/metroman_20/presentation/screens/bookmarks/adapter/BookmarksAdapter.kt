package com.one.russell.metroman_20.presentation.screens.bookmarks.adapter

import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.one.russell.metroman_20.databinding.ListItemBookmarkBinding

class BookmarksAdapter(
    onBookmarkClicked: (id: String) -> Unit,
    onRemoveClicked: (id: String) -> Unit,
) : AsyncListDifferDelegationAdapter<BookmarkListItem>(
    DiffCallback,
    bookmarkAdapterDelegate(onBookmarkClicked, onRemoveClicked)
)

fun bookmarkAdapterDelegate(
    onBookmarkClicked: (id: String) -> Unit,
    onRemoveClicked: (id: String) -> Unit,
) = adapterDelegateViewBinding<BookmarkListItem, BookmarkListItem, ListItemBookmarkBinding>(
    viewBinding = { layoutInflater, root ->
        ListItemBookmarkBinding.inflate(layoutInflater, root, false)
    },
    block = {
        binding.btnBookmark.setOnClickListener {
            onBookmarkClicked(item.id)
        }
        bind {
            // todo
        }
    }
)
