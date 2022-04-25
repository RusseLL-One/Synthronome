package com.one.russell.metroman_20.presentation.views.rotary_knob

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt
import androidx.core.graphics.withRotation
import com.one.russell.metroman_20.R
import com.one.russell.metroman_20.domain.Constants
import com.one.russell.metroman_20.presentation.views.utils.centerX
import com.one.russell.metroman_20.presentation.views.utils.centerY

class RotaryKnobView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : KnobView(context, attrs, defStyleAttr) {

    private var dashDrawable = GlowingPointerDrawable()
    private var ringDrawable = GlowingRingDrawable()

    private var glowIntense: Float = 0f
    private val glowAnimator = ValueAnimator
        .ofFloat(0f, 1f)
        .setDuration(200)
        .apply {
            interpolator = LinearInterpolator()
            addUpdateListener {
                glowIntense = it.animatedValue as Float

                dashDrawable.setGlowIntense(glowIntense)
                ringDrawable.setGlowIntense(glowIntense)
                invalidate()
            }
        }

    init {
        setBackgroundResource(R.drawable.bg_bordered_gradient_circle)
    }

    fun setupPaints(@ColorInt startColor: Int, @ColorInt endColor: Int) {
        dashDrawable.init(startColor, endColor)
        ringDrawable.init(width, height, startColor, endColor)
        invalidate()
    }

    fun setGlowIntense(bpm: Int) {
        if (glowAnimator.isStarted) {
            glowAnimator.end()
        }

        glowAnimator.setFloatValues(glowIntense, calcGlowIntense(bpm))
        glowAnimator.start()
    }

    private fun calcGlowIntense(bpm: Int): Float {
        val intense: Float = (bpm.toFloat() - Constants.MIN_BPM) / (Constants.MAX_BPM / 2 - Constants.MIN_BPM)
        return intense.coerceIn(0f, 1f)
    }

    override fun onDraw(canvas: Canvas) {
        ringDrawable.draw(canvas)

        canvas.withRotation(degrees = currentDegrees, pivotX = centerX, pivotY = centerY) {
            dashDrawable.draw(canvas)
        }

        super.onDraw(canvas)
    }

}