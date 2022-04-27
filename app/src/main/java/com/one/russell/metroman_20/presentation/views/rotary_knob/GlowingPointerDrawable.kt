package com.one.russell.metroman_20.presentation.views.rotary_knob

import android.graphics.*
import androidx.annotation.ColorInt
import androidx.core.graphics.withRotation
import androidx.core.graphics.withTranslation
import com.one.russell.metroman_20.presentation.views.utils.*
import com.one.russell.metroman_20.toPx
import kotlin.math.cos
import kotlin.math.sin

class GlowingPointerDrawable {
    private val pointerStrokeWidth: Float = 2.toPx()
    private val pointerRadius: Float = 7.toPx()
    private val glowRadius = 15.toPx()

    private var pointerPaint = Paint()
    private var pointerStrokePaint = Paint()
    private var glowPaint = Paint()

    private var pointerPosition = PointF()
    private var glowIntense: Float = 1f
    private var ringOffset: Float = 0f

    fun initPaints(width: Int, height: Int, ringOffset: Float, @ColorInt startColor: Int, @ColorInt endColor: Int) {
        this.ringOffset = ringOffset

        pointerPaint = createGradientPaint(
            gradientOrientation = GradientOrientation.LEFT_RIGHT,
            width = pointerRadius * 2,
            height = pointerRadius * 2,
            startColor = startColor,
            endColor = endColor,
            alpha = 1f,
            style = Paint.Style.FILL
        )
        pointerStrokePaint = createGradientPaint(
            gradientOrientation = GradientOrientation.BOTTOM_TOP,
            width = pointerRadius * 2,
            height = pointerRadius * 2,
            startColor = startColor,
            endColor = endColor,
            alpha = 1f,
            strokeWidth = pointerStrokeWidth
        )
        glowPaint = createGradientPaint(
            gradientOrientation = GradientOrientation.LEFT_RIGHT,
            width = width.toFloat() - ringOffset * 2,
            height = height.toFloat() - ringOffset * 2,
            startColor = startColor,
            endColor = endColor,
            alpha = glowIntense,
            strokeWidth = pointerStrokeWidth + glowRadius
        ).apply {
            maskFilter = BlurMaskFilter(glowRadius, BlurMaskFilter.Blur.NORMAL)
        }
    }

    fun setGlowIntense(intense: Float) {
        glowIntense = intense
        glowPaint.alpha = (intense * 255).toInt()
    }

    fun draw(canvas: Canvas, currentDegrees: Float) {
        canvas.drawPointer(pointerPaint, currentDegrees)
        canvas.drawPointer(pointerStrokePaint, currentDegrees)
        canvas.drawGlow(glowPaint, currentDegrees)
    }

    private fun Canvas.drawPointer(paint: Paint, degrees: Float) {
        withRotation(degrees, width.toFloat() / 2, height.toFloat() / 2) {
            withTranslation(
                width.toFloat() / 2 - pointerRadius,
                ringOffset - pointerRadius
            ) {
                drawOval(
                    0f,
                    0f,
                    pointerRadius * 2,
                    pointerRadius * 2,
                    paint
                )
            }
        }
    }

    private fun Canvas.drawGlow(paint: Paint, degrees: Float) {
        pointerPosition.calcBallPosition(width, height, degrees)
        withTranslation(ringOffset, ringOffset) {
            drawCircle(
                pointerPosition.x - ringOffset,
                pointerPosition.y - ringOffset,
                pointerRadius,
                paint
            )
        }
    }

    private fun PointF.calcBallPosition(width: Int, height: Int, degrees: Float) {
        val centerX = width.toFloat() / 2
        val centerY = height.toFloat() / 2
        val radius = centerY - ringOffset
        x = centerX + radius * sin(2 * Math.PI * degrees / 360).toFloat()
        y = centerY - radius * cos(2 * Math.PI * degrees / 360).toFloat()
    }
}