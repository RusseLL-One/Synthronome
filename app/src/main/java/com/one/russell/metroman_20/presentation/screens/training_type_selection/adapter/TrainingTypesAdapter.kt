package com.one.russell.metroman_20.presentation.screens.training_type_selection.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.one.russell.metroman_20.databinding.ItemTrainingTypeBinding

class TrainingTypesAdapter(
    private val onClick: (TrainingTypeItem) -> Unit
) : ListAdapter<TrainingTypeItem, TrainingTypeViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingTypeViewHolder {
        return TrainingTypeViewHolder(
            ItemTrainingTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClick
        )
    }

    override fun onBindViewHolder(holder: TrainingTypeViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)
    }
}