package com.one.russell.metroman_20.presentation.screens.training.training_subtype_selection.adapter

import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.one.russell.metroman_20.databinding.ItemTrainingTypeBinding

class TrainingSubtypesAdapter(
    onClick: (TrainingSubtypeItem) -> Unit
) : AsyncListDifferDelegationAdapter<TrainingSubtypeItem>(
    DiffCallback,
    trainingSubtypeAdapterDelegate(onClick)
)

fun trainingSubtypeAdapterDelegate(
    onClick: (TrainingSubtypeItem) -> Unit
) = adapterDelegateViewBinding<TrainingSubtypeItem, TrainingSubtypeItem, ItemTrainingTypeBinding>(
    viewBinding = { layoutInflater, root ->
        ItemTrainingTypeBinding.inflate(layoutInflater, root, false)
    },
    block = {
        itemView.setOnClickListener { onClick(item) }
        bind {
            binding.tvText.text = context.getString(item.textRes)
        }
    }
)