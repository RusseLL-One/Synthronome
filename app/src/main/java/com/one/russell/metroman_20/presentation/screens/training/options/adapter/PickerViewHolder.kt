package com.one.russell.metroman_20.presentation.screens.training.options.adapter

import androidx.recyclerview.widget.RecyclerView
import com.one.russell.metroman_20.databinding.ListItemPickerBinding

class PickerViewHolder(
    private val binding: ListItemPickerBinding
) : RecyclerView.ViewHolder(binding.root) {

    init {
        setIsRecyclable(false)
    }

    fun bind(item: PickerItem) {
        binding.title.text = binding.root.context.getString(item.titleRes)

        val minValue = 0
        val maxValue = (item.maxValue - item.minValue) / item.step
        val initValue = (item.initValue - item.minValue) / item.step

        binding.picker.wrapSelectorWheel = false
        binding.picker.minValue = minValue
        binding.picker.maxValue = maxValue
        binding.picker.value = initValue
        binding.picker.displayedValues = IntRange(item.minValue, item.maxValue)
            .step(item.step)
            .map { it.toString() }
            .toTypedArray()

        binding.picker.setOnValueChangedListener { _, _, newVal ->
            item.onValueChanged.invoke(newVal * item.step + item.minValue)
        }
    }
}