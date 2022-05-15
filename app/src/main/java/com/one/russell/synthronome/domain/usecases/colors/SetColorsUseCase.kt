package com.one.russell.synthronome.domain.usecases.colors

import android.content.Context
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.core.graphics.ColorUtils
import androidx.core.graphics.luminance
import com.one.russell.synthronome.R
import com.one.russell.synthronome.domain.providers.ColorsProvider
import com.one.russell.synthronome.getColorCompat

class SetColorsUseCase(
    private val context: Context,
    private val colorsProvider: ColorsProvider
) {

    fun primary(@ColorInt primary: Int) {
        colorsProvider.changeValue {
            copy(
                colorPrimary = primary,
                colorSecondary = calcColorSecondary(primary),
                colorOnPrimary = calcColorOnPrimary(primary)
            )
        }
    }

    fun backgroundBrightness(@FloatRange(from = 0.0, to = 1.0) lightness: Float) {
        colorsProvider.changeValue {
            copy(
                colorBackground = colorBackground.setColorLightness(lightness),
                colorOnBackground = calcColorOnBackground(colorBackground.setColorLightness(lightness))
            )
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
    private fun calcColorSecondary(@ColorInt colorPrimary: Int): Int {
        return colorPrimary.correctHSL(22f, 0f, 17f)
    }

    @ColorInt
    private fun calcColorOnPrimary(@ColorInt colorPrimary: Int): Int {
        return if (colorPrimary.luminance > 0.5f)
            context.getColorCompat(R.color.color_on_primary_dark)
        else
            context.getColorCompat(R.color.color_on_primary_light)
    }

    @ColorInt
    private fun calcColorOnBackground(@ColorInt colorBackground: Int): Int {
        return if (colorBackground.luminance > 0.5f)
            context.getColorCompat(R.color.color_on_background_dark)
        else
            context.getColorCompat(R.color.color_on_background_light)
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