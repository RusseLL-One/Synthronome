package com.one.russell.metroman_20.presentation.screens.training.bar_dropping_subtype_selection.adapter

import androidx.recyclerview.widget.RecyclerView
import com.one.russell.metroman_20.databinding.ItemTrainingTypeBinding

class BarDroppingSubtypeViewHolder(
    private val binding: ItemTrainingTypeBinding,
    private val onClick: (BarDroppingSubtypeItem) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var currentItem: BarDroppingSubtypeItem? = null

    init {
        itemView.setOnClickListener {
            currentItem?.let {
                onClick(it)
            }
        }
    }

    fun bind(item: BarDroppingSubtypeItem) {
        currentItem = item
        binding.tvText.text = item.text
    }
}