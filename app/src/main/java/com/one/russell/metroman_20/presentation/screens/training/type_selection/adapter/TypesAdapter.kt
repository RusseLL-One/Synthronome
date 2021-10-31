package com.one.russell.metroman_20.presentation.screens.training.type_selection.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.one.russell.metroman_20.databinding.ItemTrainingTypeBinding

class TypesAdapter(
    private val onClick: (TypeItem) -> Unit
) : ListAdapter<TypeItem, TypeViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeViewHolder {
        return TypeViewHolder(
            ItemTrainingTypeBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onClick
        )
    }

    override fun onBindViewHolder(holder: TypeViewHolder, position: Int) {
        val item = currentList[position]
        holder.bind(item)
    }
}