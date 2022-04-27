package com.one.russell.metroman_20.presentation.screens.training.training_subtype_selection.adapter

import androidx.recyclerview.widget.DiffUtil

object DiffCallback : DiffUtil.ItemCallback<TrainingSubtypeItem>() {
    override fun areItemsTheSame(oldItem: TrainingSubtypeItem, newItem: TrainingSubtypeItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: TrainingSubtypeItem, newItem: TrainingSubtypeItem): Boolean {
        return oldItem.type == newItem.type
    }
}