package com.one.russell.metroman_20.presentation.screens.training.tempo_increasing_subtype_selection.adapter

import androidx.recyclerview.widget.RecyclerView
import com.one.russell.metroman_20.databinding.ItemTrainingTypeBinding

class TempoIncreasingSubtypeViewHolder(
    private val binding: ItemTrainingTypeBinding,
    private val onClick: (TempoIncreasingSubtypeItem) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var currentItem: TempoIncreasingSubtypeItem? = null

    init {
        itemView.setOnClickListener {
            currentItem?.let {
                onClick(it)
            }
        }
    }

    fun bind(item: TempoIncreasingSubtypeItem) {
        currentItem = item
        binding.tvText.text = item.text
    }
}