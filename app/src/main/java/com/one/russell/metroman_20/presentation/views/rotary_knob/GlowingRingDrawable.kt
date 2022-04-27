package com.one.russell.metroman_20.presentation.views.rotary_knob

import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Paint
import androidx.annotation.ColorInt
import androidx.core.graphics.withTranslation
import com.one.russell.metroman_20.presentation.views.utils.*
import com.one.russell.metroman_20.toPx

class GlowingRingDrawable {

    private val ringThickness = 3.toPx()
    private val glowRadius = 20.toPx()

    private var ringPaint: Paint? = null
    private var glowPaint: Paint? = null

    private var glowIntense: Float = 1f
    private var ringOffset = 0f

    fun initPaints(width: Int, height: Int, ringOffset: Float, @ColorInt startColor: Int, @ColorInt endColor: Int) {
        this.ringOffset = ringOffset

        ringPaint = createGradientPaint(
            gradientOrientation = GradientOrientation.LEFT_RIGHT,
            width = width.toFloat() - ringOffset * 2,
            height = height.toFloat() - ringOffset * 2,
            startColor = startColor,
            endColor = endColor,
            alpha = 1f,
            strokeWidth = ringThickness,
        )
        glowPaint = Paint(ringPaint).apply {
            alpha = (glowIntense * 255).toInt()
            strokeWidth = ringThickness + glowRadius
            maskFilter = BlurMaskFilter(glowRadius, BlurMaskFilter.Blur.NORMAL)
        }
    }

    fun setGlowIntense(intense: Float) {
        glowIntense = intense
        glowPaint?.alpha = (intense * 255).toInt()
    }

    fun draw(canvas: Canvas) {
        ringPaint?.drawRing(canvas)
        glowPaint?.drawRing(canvas)
    }

    private fun Paint.drawRing(canvas: Canvas) {
        canvas.withTranslation(ringOffset, ringOffset) {
            canvas.drawOval(
                0f,
                0f,
                canvas.width.toFloat() - ringOffset * 2,
                canvas.height.toFloat() - ringOffset * 2,
                this@drawRing
            )
        }
    }
}