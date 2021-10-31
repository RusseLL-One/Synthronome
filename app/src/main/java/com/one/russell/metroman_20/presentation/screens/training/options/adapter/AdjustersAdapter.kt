package com.one.russell.metroman_20.presentation.screens.training.options.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.one.russell.metroman_20.databinding.ListItemKnobBinding
import com.one.russell.metroman_20.databinding.ListItemPickerBinding

class AdjustersAdapter : ListAdapter<ListItem, RecyclerView.ViewHolder>(DiffCallback) {

    override fun getItemViewType(position: Int): Int {
        return when (currentList[position]) {
            is KnobItem -> TYPE_KNOB
            is PickerItem -> TYPE_PICKER
            else -> throw IllegalArgumentException("Unknown item type")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_KNOB -> KnobViewHolder(
                ListItemKnobBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            TYPE_PICKER -> PickerViewHolder(
                ListItemPickerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            )
            else -> throw IllegalArgumentException("Unknown item type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = currentList[position]
        when (item) {
            is KnobItem -> (holder as KnobViewHolder).bind(item)
            is PickerItem -> (holder as PickerViewHolder).bind(item)
        }
    }

    companion object {
        private const val TYPE_KNOB = 0
        private const val TYPE_PICKER = 1
    }
}