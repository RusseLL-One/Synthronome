package com.one.russell.synthronome.presentation.screens.training.options.adapter

import com.hannesdorfmann.adapterdelegates4.AsyncListDifferDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import com.one.russell.synthronome.R
import com.one.russell.synthronome.databinding.ListItemKnobBinding
import com.one.russell.synthronome.databinding.ListItemPickerBinding
import com.one.russell.synthronome.presentation.screens.training.options.ControlType
import com.one.russell.synthronome.presentation.screens.training.options.OptionsAdjusterType

class AdjustersAdapter(
    onValueChanged: (type: OptionsAdjusterType, value: Int) -> Unit
) : AsyncListDifferDelegationAdapter<AdjusterListItem>(
    DiffCallback,
    knobAdapterDelegate(onValueChanged),
    pickerAdapterDelegate(onValueChanged)
)

fun knobAdapterDelegate(
    onValueChanged: (type: OptionsAdjusterType, valueDelta: Int) -> Unit
) = adapterDelegateViewBinding<AdjusterListItem, AdjusterListItem, ListItemKnobBinding>(
    viewBinding = { layoutInflater, root ->
        ListItemKnobBinding.inflate(layoutInflater, root, false)
    },
    on = { item, _, _ -> item.type.controlType == ControlType.KNOB },
    block = {
        binding.vKnob.addOnValueChangedCallback {
            onValueChanged(item.type, it)
        }
        bind { payload ->
            if (Payload.BPM_CHANGED in payload) {
                binding.tvValue.text = binding.root.context.getString(R.string.main_bpm, item.value)
                binding.vKnob.setGlowIntense(item.value)
                return@bind
            }
            binding.tvTitle.text = binding.root.context.getString(item.type.titleRes)
            binding.tvTitle.setTextColor(item.colorOnBackground)
            binding.tvValue.text = binding.root.context.getString(R.string.main_bpm, item.value)
            binding.tvValue.setTextColor(item.colorOnBackground)
            binding.vKnob.setGlowIntense(item.value)
            binding.vKnob.setupPaints(item.colorPrimary, item.colorSecondary)
        }
    }
)

fun pickerAdapterDelegate(
    onValueChanged: (type: OptionsAdjusterType, value: Int) -> Unit
) = adapterDelegateViewBinding<AdjusterListItem, AdjusterListItem, ListItemPickerBinding>(
    viewBinding = { layoutInflater, root ->
        ListItemPickerBinding.inflate(layoutInflater, root, false)
    },
    on = { item, _, _ -> item.type.controlType == ControlType.PICKER },
    block = {
        binding.npPicker.view.setOnValueChangedListener { _, _, newVal ->
            onValueChanged(item.type, newVal * item.type.step + item.type.minValue)
        }
        bind {
            binding.tvTitle.text = binding.root.context.getString(item.type.titleRes)

            val minValue = 0
            val maxValue = (item.type.maxValue - item.type.minValue) / item.type.step
            val initValue = (item.value - item.type.minValue) / item.type.step

            binding.npPicker.view.wrapSelectorWheel = false
            binding.npPicker.view.minValue = minValue
            binding.npPicker.view.maxValue = maxValue
            binding.npPicker.view.value = initValue
            binding.npPicker.view.displayedValues = IntRange(item.type.minValue, item.type.maxValue)
                .step(item.type.step)
                .map { it.toString() }
                .toTypedArray()

            binding.tvTitle.setTextColor(item.colorOnBackground)
            binding.npPicker.setTextColor(item.colorOnBackground)
            binding.npPicker.setupPaints(item.colorPrimary, item.colorSecondary)
        }
    }
)

enum class Payload {
    BPM_CHANGED
}