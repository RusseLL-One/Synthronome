package com.one.russell.metroman_20.presentation.screens.training.tempo_increasing_subtype_selection.adapter

import androidx.recyclerview.widget.DiffUtil

object DiffCallback : DiffUtil.ItemCallback<TempoIncreasingSubtypeItem>() {
    override fun areItemsTheSame(oldItem: TempoIncreasingSubtypeItem, newItem: TempoIncreasingSubtypeItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: TempoIncreasingSubtypeItem, newItem: TempoIncreasingSubtypeItem): Boolean {
        return oldItem.type == newItem.type
    }
}