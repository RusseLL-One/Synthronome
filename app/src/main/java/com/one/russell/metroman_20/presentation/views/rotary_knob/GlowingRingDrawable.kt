package com.one.russell.metroman_20.presentation.views.rotary_knob

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.annotation.ColorInt
import com.one.russell.metroman_20.presentation.views.utils.createBevelPaint
import com.one.russell.metroman_20.presentation.views.utils.createGlowPaint
import com.one.russell.metroman_20.presentation.views.utils.createPaint

class GlowingRingDrawable {
    private var centerX: Float = 0f
    private var centerY: Float = 0f

    private val radius: Float
        get() = centerX - RING_THICKNESS_PX / 2
    private val innerBevelRadius: Float
        get() = radius - RING_THICKNESS_PX / 2 + INNER_BEVEL_THICKNESS_PX / 2
    private val outerBevelRadius: Float
        get() = radius + RING_THICKNESS_PX / 2 - OUTER_BEVEL_THICKNESS_PX / 2

    private var ringPaint: Paint? = null
    private var innerBevelPaint: Paint? = null
    private var outerBevelPaint: Paint? = null
    private var glowPaint: Paint? = null

    private var glowIntense: Float = 1f

    fun init(centerX: Float, centerY: Float, @ColorInt initColor: Int) {
        this.centerX = centerX
        this.centerY = centerY

        initPaints(initColor)
    }

    private fun initPaints(@ColorInt initColor: Int) {
        ringPaint = createPaint(
            color = initColor,
            brightness = RING_BRIGHTNESS,
            strokeThickness = RING_THICKNESS_PX
        )
        innerBevelPaint = createBevelPaint(
            centerX = centerX,
            centerY = centerY,
            radius = radius,
            alpha = BEVEL_ALPHA,
            startColor = Color.BLACK,
            strokeThickness = INNER_BEVEL_THICKNESS_PX,
            endColor = Color.WHITE
        )
        outerBevelPaint = createBevelPaint(
            centerX = centerX,
            centerY = centerY,
            radius = radius,
            alpha = BEVEL_ALPHA,
            startColor = Color.WHITE,
            strokeThickness = OUTER_BEVEL_THICKNESS_PX,
            endColor = Color.BLACK
        )
        glowPaint = createGlowPaint(
            color = initColor,
            thickness = RING_THICKNESS_PX,
            glowRadius = GLOW_RADIUS,
            glowIntense = glowIntense
        )
    }

    fun setGlowIntense(intense: Float) {
        glowIntense = intense
        glowPaint?.alpha = (intense * 255).toInt()
    }

    fun draw(canvas: Canvas) {
        ringPaint?.drawRing(canvas, radius)
        innerBevelPaint?.drawRing(canvas, innerBevelRadius)
        outerBevelPaint?.drawRing(canvas, outerBevelRadius)
        glowPaint?.drawRing(canvas, radius)
    }

    private fun Paint.drawRing(canvas: Canvas, radius: Float) {
        canvas.drawCircle(
            centerX,
            centerY,
            radius,
            this
        )
    }

    companion object {
        private const val RING_THICKNESS_PX = 60f
        private const val GLOW_RADIUS = 40f
        private const val OUTER_BEVEL_THICKNESS_PX = 8f
        private const val INNER_BEVEL_THICKNESS_PX = 4f
        private const val BEVEL_ALPHA = 0.7f
        private const val RING_BRIGHTNESS = 0.3f
    }
}