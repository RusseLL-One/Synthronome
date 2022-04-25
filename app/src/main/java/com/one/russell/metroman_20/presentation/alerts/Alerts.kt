package com.one.russell.metroman_20.presentation.alerts

import android.R
import android.content.Context
import android.content.DialogInterface
import com.skydoves.colorpickerview.ColorPickerDialog
import com.skydoves.colorpickerview.flag.BubbleFlag
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener

// todo доработать алерт
fun showColorPickerDialog(context: Context, onColorPicked: (Int) -> Unit) {
    ColorPickerDialog.Builder(context)
        .setTitle("ColorPicker Dialog")
        .setPreferenceName("MyColorPickerDialog")
        .setPositiveButton(context.getString(R.string.ok),
            ColorEnvelopeListener { envelope, fromUser -> onColorPicked(envelope.color) })
        .setNegativeButton(context.getString(R.string.cancel)) { dialogInterface, i -> dialogInterface.dismiss() }
        .attachAlphaSlideBar(false)
        .attachBrightnessSlideBar(true)
        .setBottomSpace(12)
        .apply { colorPickerView.flagView = BubbleFlag(context) }
        .show()
}