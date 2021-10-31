package com.one.russell.metroman_20.presentation.screens.training.tempo_increasing_subtype_selection.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.one.russell.metroman_20.databinding.ItemTrainingTypeBinding

class TempoIncreasingSubtypesAdapter(
    private val onClick: (TempoIncreasingSubtypeItem) -> Unit
) : ListAdapter<TempoIncreasingSubtypeItem, TempoIncreasingSubtypeViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TempoIncreasingSubtypeViewHolder {
        return TempoIncreasingSubtypeViewHolder(
            ItemTrainingTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClick
        )
    }

    override fun onBindViewHolder(holder: TempoIncreasingSubtypeViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)
    }
}