package com.one.russell.metroman_20.presentation.screens.bookmarks.adapter

import androidx.recyclerview.widget.DiffUtil

object DiffCallback : DiffUtil.ItemCallback<BookmarkListItem>() {
    override fun areItemsTheSame(oldItem: BookmarkListItem, newItem: BookmarkListItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: BookmarkListItem, newItem: BookmarkListItem): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: BookmarkListItem, newItem: BookmarkListItem): Any? {
        return if (oldItem.bpm == newItem.bpm &&
            oldItem.timeSignature == newItem.timeSignature &&
            oldItem.isSelected != newItem.isSelected
        ) Payload.BOOKMARK_SELECTED
        else null
    }
}