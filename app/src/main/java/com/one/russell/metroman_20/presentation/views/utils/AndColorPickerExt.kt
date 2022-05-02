package com.one.russell.metroman_20.presentation.views.utils

import androidx.annotation.ColorInt
import codes.side.andcolorpicker.converter.setFromColorInt
import codes.side.andcolorpicker.hsl.HSLColorPickerSeekBar
import codes.side.andcolorpicker.model.IntegerHSLColor
import codes.side.andcolorpicker.view.picker.ColorSeekBar

fun HSLColorPickerSeekBar.addListener(callback: (IntegerHSLColor) -> Unit) {
    addListener(object :
        ColorSeekBar.DefaultOnColorPickListener<ColorSeekBar<IntegerHSLColor>, IntegerHSLColor>() {
        override fun onColorChanged(
            picker: ColorSeekBar<IntegerHSLColor>,
            color: IntegerHSLColor,
            value: Int
        ) {
            callback(color)
        }
    })
}

fun createHSLColor(@ColorInt color: Int): IntegerHSLColor {
    return IntegerHSLColor().also { hsl -> hsl.setFromColorInt(color) }
}
