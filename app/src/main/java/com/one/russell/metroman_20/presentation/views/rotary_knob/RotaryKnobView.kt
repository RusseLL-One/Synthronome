package com.one.russell.metroman_20.presentation.views.rotary_knob

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt
import com.one.russell.metroman_20.R
import com.one.russell.metroman_20.domain.Constants
import com.one.russell.metroman_20.getColorCompat
import com.one.russell.metroman_20.getStyledAttributes
import com.one.russell.metroman_20.toPx

class RotaryKnobView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : KnobView(context, attrs, defStyleAttr) {

    private var ringOffset = 17.toPx()

    private var pointerDrawable = GlowingPointerDrawable()
    private var ringDrawable = GlowingRingDrawable()

    @ColorInt private var colorPrimary: Int = 0
    @ColorInt private var colorSecondary: Int = 0

    private var glowIntense: Float = 0f
    private val glowAnimator = ValueAnimator
        .ofFloat(0f, 1f)
        .setDuration(200)
        .apply {
            interpolator = LinearInterpolator()
            addUpdateListener {
                glowIntense = it.animatedValue as Float

                pointerDrawable.setGlowIntense(glowIntense)
                ringDrawable.setGlowIntense(glowIntense)
                invalidate()
            }
        }

    init {
        initAttrs(context, attrs, defStyleAttr)
        setBackgroundResource(R.drawable.bg_bordered_gradient_circle)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?, defStyle: Int) {
        context.getStyledAttributes(attrs, R.styleable.RotaryKnobView, defStyle) {
            ringOffset = getDimension(R.styleable.RotaryKnobView_ringOffset, ringOffset)
        }
    }

    fun setupPaints(@ColorInt startColor: Int, @ColorInt endColor: Int) = post {
        colorPrimary = startColor
        colorSecondary = endColor
        initDrawablesPaints()
    }

    override fun doOnBlock() {
        initDrawablesPaints()
    }

    private fun initDrawablesPaints() {
        val startColor = if (!isBlocked) colorPrimary
        else context.getColorCompat(R.color.blocked_knob_color_1)
        val endColor = if (!isBlocked) colorSecondary
        else context.getColorCompat(R.color.blocked_knob_color_2)

        pointerDrawable.initPaints(width, height, ringOffset, startColor, endColor)
        ringDrawable.initPaints(width, height, ringOffset, startColor, endColor)
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
        pointerDrawable.draw(canvas, currentDegrees)
        super.onDraw(canvas)
    }

}