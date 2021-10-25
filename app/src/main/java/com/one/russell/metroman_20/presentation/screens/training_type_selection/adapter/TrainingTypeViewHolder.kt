package com.one.russell.metroman_20.presentation.screens.training_type_selection.adapter

import androidx.recyclerview.widget.RecyclerView
import com.one.russell.metroman_20.databinding.ItemTrainingTypeBinding

class TrainingTypeViewHolder(
    private val binding: ItemTrainingTypeBinding,
    private val onClick: (TrainingTypeItem) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var currentItem: TrainingTypeItem? = null

    init {
        itemView.setOnClickListener {
            currentItem?.let {
                onClick(it)
            }
        }
    }

    fun bind(item: TrainingTypeItem) {
        currentItem = item
        binding.tvText.text = item.text
    }
}