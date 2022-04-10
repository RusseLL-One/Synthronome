package com.one.russell.metroman_20.presentation.views.rotary_knob

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import androidx.core.graphics.withRotation
import androidx.core.view.doOnLayout
import com.one.russell.metroman_20.R
import com.one.russell.metroman_20.domain.Constants
import com.one.russell.metroman_20.presentation.views.utils.centerX
import com.one.russell.metroman_20.presentation.views.utils.centerY

class RotaryKnobView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : KnobView(context, attrs, defStyleAttr) {

    private val primaryColor: Int = ContextCompat.getColor(context, R.color.primary)

    private var dashDrawable = GlowingDashDrawable()
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
        doOnLayout {
            initDrawables()
        }
    }

    private fun initDrawables() {
        dashDrawable.init(centerX, centerY, primaryColor)
        ringDrawable.init(centerX, centerY, primaryColor)
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
        canvas.withRotation(degrees = currentDegrees, pivotX = centerX, pivotY = centerY) {
            dashDrawable.draw(canvas)
        }

        ringDrawable.draw(canvas)

        super.onDraw(canvas)
    }

}