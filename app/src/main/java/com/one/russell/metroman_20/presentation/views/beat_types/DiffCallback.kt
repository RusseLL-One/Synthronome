package com.one.russell.metroman_20.presentation.views.beat_types

import androidx.recyclerview.widget.DiffUtil

object DiffCallback : DiffUtil.ItemCallback<BeatTypeItem>() {
    override fun areItemsTheSame(oldItem: BeatTypeItem, newItem: BeatTypeItem): Boolean {
        return oldItem.index == newItem.index
    }

    override fun areContentsTheSame(oldItem: BeatTypeItem, newItem: BeatTypeItem): Boolean {
        return oldItem == newItem
    }

    override fun getChangePayload(oldItem: BeatTypeItem, newItem: BeatTypeItem): Any? {
        return if (
            oldItem.primaryColor == newItem.primaryColor &&
            oldItem.secondaryColor == newItem.secondaryColor &&
            oldItem.beatType != newItem.beatType
        ) Payload.ANIMATE_BEAT_TYPE_CHANGE
        else null
    }
}