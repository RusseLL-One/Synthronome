package com.one.russell.metroman_20.presentation.views.rotary_knob

import android.graphics.Canvas
import android.graphics.Paint
import androidx.annotation.ColorInt
import androidx.core.graphics.withTranslation
import com.one.russell.metroman_20.presentation.views.utils.*
import com.one.russell.metroman_20.toPx

class GlowingRingDrawable {

    private val ringThickness = 3.toPx()
    private val glowRadius = 20.toPx()
    private val offsetWidth = 17.toPx()

    private var ringPaint: Paint? = null
    private var glowPaint: Paint? = null

    private var glowIntense: Float = 1f

    fun init(width: Int, height: Int, @ColorInt startColor: Int, @ColorInt endColor: Int) {
        setPaints(width, height, startColor, endColor)
    }

    private fun setPaints(width: Int, height: Int, @ColorInt startColor: Int, @ColorInt endColor: Int) {
        ringPaint = createGradientPaint(
            gradientOrientation = GradientOrientation.LEFT_RIGHT,
            width = width.toFloat() - offsetWidth * 2,
            height = height.toFloat() - offsetWidth * 2,
            startColor = startColor,
            endColor = endColor,
            alpha = 1f,
            strokeWidth = ringThickness,
        )
        glowPaint = createGlowPaint(
            color = startColor,
            thickness = ringThickness,
            glowRadius = glowRadius,
            glowIntense = glowIntense
        )
    }

    fun setGlowIntense(intense: Float) {
        glowIntense = intense
        glowPaint?.alpha = (intense * 255).toInt()
    }

    fun draw(canvas: Canvas) {
        canvas.withTranslation(offsetWidth, offsetWidth) { // todo сделать красивее
            canvas.drawOval(
                0f,
                0f,
                canvas.width.toFloat() - offsetWidth * 2,
                canvas.height.toFloat() - offsetWidth * 2,
                ringPaint ?: Paint()
            )
        }
        glowPaint?.drawRing(canvas)
    }

    private fun Paint.drawRing(canvas: Canvas) {
        canvas.drawOval(
            offsetWidth,
            offsetWidth,
            canvas.width.toFloat() - offsetWidth,
            canvas.height.toFloat() - offsetWidth,
            this
        )
    }
}