package com.one.russell.metroman_20.presentation.screens.training.options.adapter

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.one.russell.metroman_20.R
import com.one.russell.metroman_20.databinding.ListItemKnobBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class KnobViewHolder(
    private val binding: ListItemKnobBinding
) : RecyclerView.ViewHolder(binding.root) {

    init {
        setIsRecyclable(false)
    }

    fun bind(item: KnobItem) {
        binding.tvTitle.text = binding.root.context.getString(item.titleRes)
        binding.vKnob.addOnValueChangedCallback {
            item.onValueChanged.invoke(it)
        }

        (binding.root.context as LifecycleOwner).lifecycleScope.launch {
            item.valueStateFlow.collect {
                binding.tvValue.text = binding.root.context.getString(R.string.main_bpm, it)
                binding.vKnob.setGlowIntense(it)
            }
        }
    }
}