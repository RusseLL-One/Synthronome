package com.one.russell.metroman_20.presentation.screens.training_type_selection.adapter

import androidx.recyclerview.widget.DiffUtil

object DiffCallback : DiffUtil.ItemCallback<TrainingTypeItem>() {
    override fun areItemsTheSame(oldItem: TrainingTypeItem, newItem: TrainingTypeItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: TrainingTypeItem, newItem: TrainingTypeItem): Boolean {
        return oldItem.type == newItem.type
    }
}