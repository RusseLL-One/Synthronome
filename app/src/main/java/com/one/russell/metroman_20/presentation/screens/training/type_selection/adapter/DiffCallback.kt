package com.one.russell.metroman_20.presentation.screens.training.type_selection.adapter

import androidx.recyclerview.widget.DiffUtil

object DiffCallback : DiffUtil.ItemCallback<TypeItem>() {
    override fun areItemsTheSame(oldItem: TypeItem, newItem: TypeItem): Boolean {
        return oldItem.type == newItem.type
    }

    override fun areContentsTheSame(oldItem: TypeItem, newItem: TypeItem): Boolean {
        return oldItem == newItem
    }
}