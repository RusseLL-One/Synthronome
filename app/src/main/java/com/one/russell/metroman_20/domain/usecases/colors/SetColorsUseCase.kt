package com.one.russell.metroman_20.domain.usecases.colors

import androidx.annotation.ColorInt
import androidx.core.graphics.ColorUtils
import com.one.russell.metroman_20.domain.providers.ColorsProvider

class SetColorsUseCase(
    private val colorsProvider: ColorsProvider
) {

    fun primary(@ColorInt primary: Int) {
        val currentColors = colorsProvider.colorFlow.value
        colorsProvider.colorFlow.value = currentColors.copy(
            primaryColor = primary,
            secondaryColor = calcSecondaryColor(primary)
        )
    }

    fun background(@ColorInt background: Int) {
        val currentColors = colorsProvider.colorFlow.value
        colorsProvider.colorFlow.value = currentColors.copy(
            backgroundColor = background,
        )
    }

    @ColorInt
    private fun calcSecondaryColor(@ColorInt primaryColor: Int): Int {
        return primaryColor.correctHSL(22f, 0f, 17f)
    }

    @ColorInt
    private fun Int.correctHSL(dH: Float, dS: Float, dL: Float): Int {
        val hslArray = FloatArray(3).apply { ColorUtils.colorToHSL(this@correctHSL, this) }
        hslArray[0] = (hslArray[0] + dH).coerceIn(0f, 359f)
        hslArray[1] = (hslArray[1] + dS / 255).coerceIn(0f, 1f)
        hslArray[2] = (hslArray[2] + dL / 255).coerceIn(0f, 1f)
        return ColorUtils.HSLToColor(hslArray)
    }
}