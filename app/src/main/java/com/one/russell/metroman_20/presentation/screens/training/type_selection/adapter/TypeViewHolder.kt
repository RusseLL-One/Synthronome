package com.one.russell.metroman_20.presentation.screens.training.type_selection.adapter

import androidx.recyclerview.widget.RecyclerView
import com.one.russell.metroman_20.databinding.ItemTrainingTypeBinding

class TypeViewHolder(
    private val binding: ItemTrainingTypeBinding,
    private val onClick: (TypeItem) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var currentItem: TypeItem? = null

    init {
        itemView.setOnClickListener {
            currentItem?.let {
                onClick(it)
            }
        }
    }

    fun bind(item: TypeItem) {
        currentItem = item
        binding.tvText.text = item.text
    }
}