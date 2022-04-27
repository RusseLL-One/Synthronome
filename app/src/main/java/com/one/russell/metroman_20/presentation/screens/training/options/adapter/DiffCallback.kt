package com.one.russell.metroman_20.presentation.screens.training.options.adapter

import androidx.recyclerview.widget.DiffUtil
import com.one.russell.metroman_20.presentation.screens.training.options.ControlType

object DiffCallback : DiffUtil.ItemCallback<AdjusterListItem>() {
    override fun areItemsTheSame(oldItem: AdjusterListItem, newItem: AdjusterListItem): Boolean {
        return oldItem.type == newItem.type
    }

    override fun areContentsTheSame(oldItem: AdjusterListItem, newItem: AdjusterListItem): Boolean {
        return oldItem.value == newItem.value
    }

    override fun getChangePayload(oldItem: AdjusterListItem, newItem: AdjusterListItem): Any? {
        return if (oldItem.type.controlType == ControlType.KNOB && oldItem.value != newItem.value) {
            Payload.BPM_CHANGED
        } else null
    }
}