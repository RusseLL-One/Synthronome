package com.one.russell.metroman_20.presentation.screens.training.bar_dropping_subtype_selection.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.one.russell.metroman_20.databinding.ItemTrainingTypeBinding

class BarDroppingSubtypesAdapter(
    private val onClick: (BarDroppingSubtypeItem) -> Unit
) : ListAdapter<BarDroppingSubtypeItem, BarDroppingSubtypeViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarDroppingSubtypeViewHolder {
        return BarDroppingSubtypeViewHolder(
            ItemTrainingTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClick
        )
    }

    override fun onBindViewHolder(holder: BarDroppingSubtypeViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)
    }
}