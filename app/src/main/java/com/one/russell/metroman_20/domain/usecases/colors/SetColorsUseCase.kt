package com.one.russell.metroman_20.domain.usecases.colors

import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.core.graphics.ColorUtils
import com.one.russell.metroman_20.domain.providers.ColorsProvider

class SetColorsUseCase(
    private val colorsProvider: ColorsProvider
) {

    fun primary(@ColorInt primary: Int) {
        colorsProvider.changeValue {
            copy(
                primaryColor = primary,
                secondaryColor = calcSecondaryColor(primary)
            )
        }
    }

    fun backgroundBrightness(@FloatRange(from = 0.0, to = 1.0) lightness: Float) {
        colorsProvider.changeValue {
            copy(backgroundColor = backgroundColor.setColorLightness(lightness))
        }
    }

    @ColorInt
    private fun Int.setColorLightness(@FloatRange(from = 0.0, to = 1.0) brightness: Float): Int {
        return FloatArray(3)
            .apply { ColorUtils.colorToHSL(this@setColorLightness, this) }
            .apply { this[2] = brightness }
            .let { ColorUtils.HSLToColor(it) }
    }

    @ColorInt
    private fun calcSecondaryColor(@ColorInt primaryColor: Int): Int {
        return primaryColor.correctHSL(22f, 0f, 17f)
    }

    @ColorInt
    private fun Int.correctHSL(dH: Float, dS: Float, dL: Float): Int {
        return FloatArray(3).apply { ColorUtils.colorToHSL(this@correctHSL, this) }
            .apply {
                this[0] = (this[0] + dH).coerceIn(0f, 359f)
                this[1] = (this[1] + dS / 255).coerceIn(0f, 1f)
                this[2] = (this[2] + dL / 255).coerceIn(0f, 1f)
            }.let { ColorUtils.HSLToColor(it) }
    }
}