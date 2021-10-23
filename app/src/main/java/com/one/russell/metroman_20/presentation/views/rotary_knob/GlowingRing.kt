package com.one.russell.metroman_20.presentation.views.rotary_knob

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import androidx.annotation.ColorInt
import androidx.appcompat.content.res.AppCompatResources
import com.one.russell.metroman_20.R

class GlowingRing(
    context: Context,
    width: Int,
    height: Int,
    @ColorInt initColor: Int
) {

    private var ringDrawable: ShapeDrawable? = null
    private var glowDrawable: Drawable? = null
    private var bevelDrawable: Drawable? = null

    init {
        ringDrawable = ShapeDrawable(OvalShape()).apply {
            paint.color = initColor
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = RING_THICKNESS_PX.toFloat()

            val ringOffsetWidth = (width * RING_OFFSET_RATIO).toInt()
            val ringOffsetHeight = (height * RING_OFFSET_RATIO).toInt()
            setBounds(
                ringOffsetWidth,
                ringOffsetHeight,
                width - ringOffsetWidth,
                height - ringOffsetHeight
            )
        }

        glowDrawable = AppCompatResources.getDrawable(context, R.drawable.knob_ring_glow)?.apply {
            setBounds(0, 0, width, height)
            setTint(initColor)
        }

        bevelDrawable = AppCompatResources.getDrawable(context, R.drawable.knob_ring_bevel)?.apply {
            setBounds(0, 0, width, height)
        }
    }

    // intense = 0..1
    fun setGlowIntense(intense: Float) {
        glowDrawable?.alpha = (intense.coerceIn(0f, 1f) * 255).toInt()
    }

    fun draw(canvas: Canvas) {
        ringDrawable?.draw(canvas)
        glowDrawable?.draw(canvas)
        bevelDrawable?.draw(canvas)
    }

    companion object {
        private const val RING_THICKNESS_PX = 43
        private const val RING_OFFSET_RATIO = 0.101
    }
}