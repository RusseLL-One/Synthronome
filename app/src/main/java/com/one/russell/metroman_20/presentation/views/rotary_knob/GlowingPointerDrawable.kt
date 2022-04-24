package com.one.russell.metroman_20.presentation.views.rotary_knob

import android.graphics.*
import androidx.annotation.ColorInt
import androidx.core.graphics.withTranslation
import com.one.russell.metroman_20.presentation.views.utils.GradientOrientation
import com.one.russell.metroman_20.presentation.views.utils.createGlowPaint
import com.one.russell.metroman_20.presentation.views.utils.createGradientPaint
import com.one.russell.metroman_20.toPx

class GlowingPointerDrawable {
    private val pointerOffset: Float = 17.toPx()
    private val pointerStrokeWidth: Float = 2.toPx()
    private val pointerRadius: Float = 7.toPx()
    private val glowRadius = 15.toPx()

    private var pointerPaint: Paint? = null
    private var pointerStrokePaint: Paint? = null
    private var glowPaint: Paint? = null

    private var glowIntense: Float = 1f

    fun init(@ColorInt initColor: Int) {
        pointerPaint = createGradientPaint(
            gradientOrientation = GradientOrientation.RIGHT_LEFT,
            width = pointerRadius * 2,
            height = pointerRadius * 2,
            startColor = Color.parseColor("#FFA959"),
            endColor = Color.parseColor("#D35746"),
            alpha = 1f,
            style = Paint.Style.FILL
        )
        pointerStrokePaint = createGradientPaint(
            gradientOrientation = GradientOrientation.TOP_BOTTOM,
            width = pointerRadius * 2,
            height = pointerRadius * 2,
            startColor = Color.parseColor("#FFA959"),
            endColor = Color.parseColor("#D35746"),
            alpha = 1f,
            strokeWidth = pointerStrokeWidth
        )
        glowPaint = createGlowPaint(initColor, pointerStrokeWidth, glowRadius, glowIntense)
    }

    fun setGlowIntense(intense: Float) {
        glowIntense = intense
        glowPaint?.alpha = (intense * 255).toInt()
    }

    fun draw(canvas: Canvas) {
        pointerPaint?.drawPointer(canvas)
        pointerStrokePaint?.drawPointer(canvas)
        glowPaint?.drawPointer(canvas)
    }

    private fun Paint.drawPointer(canvas: Canvas) {
        canvas.withTranslation(canvas.width.toFloat() / 2 - pointerRadius, pointerOffset - pointerRadius) {
            canvas.drawOval(0f, 0f, pointerRadius * 2, pointerRadius * 2, this@drawPointer)
        }
    }
}