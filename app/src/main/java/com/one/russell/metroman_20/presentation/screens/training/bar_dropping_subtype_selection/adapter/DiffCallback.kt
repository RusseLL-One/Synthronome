package com.one.russell.metroman_20.presentation.screens.training.bar_dropping_subtype_selection.adapter

import androidx.recyclerview.widget.DiffUtil

object DiffCallback : DiffUtil.ItemCallback<BarDroppingSubtypeItem>() {
    override fun areItemsTheSame(oldItem: BarDroppingSubtypeItem, newItem: BarDroppingSubtypeItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: BarDroppingSubtypeItem, newItem: BarDroppingSubtypeItem): Boolean {
        return oldItem.type == newItem.type
    }
}