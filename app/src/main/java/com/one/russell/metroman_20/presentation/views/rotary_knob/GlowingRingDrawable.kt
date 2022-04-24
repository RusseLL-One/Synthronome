package com.one.russell.metroman_20.presentation.views.rotary_knob

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.annotation.ColorInt
import com.one.russell.metroman_20.presentation.views.utils.*
import com.one.russell.metroman_20.toPx

class GlowingRingDrawable {

    private val ringThickness = 3.toPx()
    private val glowRadius = 20.toPx()
    private val offsetWidth = 17.toPx()

    private var ringPaint: Paint? = null
    private var glowPaint: Paint? = null

    private var glowIntense: Float = 1f

    fun init(width: Int, height: Int, @ColorInt initColor: Int) {
        initPaints(width, height, initColor)
    }

    private fun initPaints(width: Int, height: Int, @ColorInt initColor: Int) {
        ringPaint = createGradientPaint(
            gradientOrientation = GradientOrientation.LEFT_RIGHT,
            width = width.toFloat(),
            height = height.toFloat(),
            startColor = Color.parseColor("#D35746"),
            endColor = Color.parseColor("#FFA959"),
            alpha = 1f,
            strokeWidth = ringThickness,
        )
        glowPaint = createGlowPaint(
            color = initColor,
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
        ringPaint?.drawRing(canvas)
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