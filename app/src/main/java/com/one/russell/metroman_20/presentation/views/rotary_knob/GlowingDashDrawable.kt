package com.one.russell.metroman_20.presentation.views.rotary_knob

import android.graphics.*
import androidx.annotation.ColorInt
import com.one.russell.metroman_20.presentation.views.utils.createGlowPaint
import com.one.russell.metroman_20.presentation.views.utils.createPaint
import com.one.russell.metroman_20.toPx

class GlowingDashDrawable {
    private var centerX: Float = 0f
    private var centerY: Float = 0f

    private val dashOffset: Float = 25.toPx()
    private val dashHeight: Float = 25.toPx()

    private var dashPaint: Paint? = null
    private var glowPaint: Paint? = null

    private var glowIntense: Float = 1f

    fun init(centerX: Float, centerY: Float, @ColorInt initColor: Int) {
        this.centerX = centerX
        this.centerY = centerY

        dashPaint = createPaint(initColor, DASH_BRIGHTNESS, DASH_THICKNESS)
        glowPaint = createGlowPaint(initColor, DASH_THICKNESS, GLOW_RADIUS, glowIntense)
    }

    fun setGlowIntense(intense: Float) {
        glowIntense = intense
        glowPaint?.alpha = (intense * 255).toInt()
    }

    fun draw(canvas: Canvas) {
        dashPaint?.drawDash(canvas)
        glowPaint?.drawDash(canvas)
    }

    private fun Paint.drawDash(canvas: Canvas) {
        canvas.drawLine(
            centerX,
            dashOffset,
            centerX,
            dashOffset + dashHeight,
            this
        )
    }

    companion object {
        private const val DASH_THICKNESS = 12f
        private const val GLOW_RADIUS = 20f

        private const val DASH_BRIGHTNESS = 0.3f
    }
}