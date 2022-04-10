package com.one.russell.metroman_20.presentation.views.tap_button

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.content.res.getDimensionPixelSizeOrThrow
import androidx.core.view.doOnLayout
import com.one.russell.metroman_20.R
import com.one.russell.metroman_20.domain.Constants.MAX_BPM
import com.one.russell.metroman_20.domain.Constants.MIN_BPM
import com.one.russell.metroman_20.getStyledAttributes
import com.one.russell.metroman_20.presentation.views.utils.centerX
import com.one.russell.metroman_20.presentation.views.utils.centerY
import com.one.russell.metroman_20.presentation.views.utils.createGlowPaint
import com.one.russell.metroman_20.presentation.views.utils.createPaint

class TapButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var onTapClickedListener: ((Int?) -> Unit)? = null

    private var prevTouchTime = 0L
    private var prevTouchInterval = 0L
    private var isFirstClick = true

    private var radius: Float = 0f

    private var circlePaint: Paint? = null
    private var glowPaint: Paint? = null
    private val glowAnimator = ValueAnimator
        .ofFloat(0f, MAX_GLOW_ALPHA, 0f)
        .setDuration(400)
        .apply {
            interpolator = DecelerateInterpolator()
            addUpdateListener {
                glowPaint?.alpha = (it.animatedValue as Float * 255).toInt()
                invalidate()
            }
        }

    init {
        initAttrs(context, attrs, defStyleAttr)
        doOnLayout {
            initPaints(ContextCompat.getColor(context, R.color.primary))
        }
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?, defStyle: Int) {
        context.getStyledAttributes(attrs, R.styleable.TapButtonView, defStyle) {
            radius = getDimensionPixelSizeOrThrow(R.styleable.TapButtonView_radius).toFloat()
        }
    }

    private fun initPaints(@ColorInt initColor: Int) {
        circlePaint = createPaint(initColor, CIRCLE_BRIGHTNESS, 0f, Paint.Style.FILL)
        glowPaint = createGlowPaint(initColor, 0f, GLOW_RADIUS, 0f, Paint.Style.FILL)
    }

    override fun onDraw(canvas: Canvas) {
        circlePaint?.drawTapButton(canvas, radius)
        glowPaint?.drawTapButton(canvas, radius + GLOW_RADIUS)
    }

    private fun Paint.drawTapButton(canvas: Canvas, radius: Float) {
        canvas.drawCircle(centerX, centerY, radius, this)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = 2 * (radius + 2 * GLOW_RADIUS).toInt()

        val finalMeasureSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY)
        super.onMeasure(finalMeasureSpec, finalMeasureSpec)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                performClick()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        val interval = System.currentTimeMillis() - prevTouchTime
        animateClick()

        when {
            interval < MIN_TOUCH_INTERVAL -> {
                //Если интервал нажатий слишком маленький, устанавливаем его минимальное значение
                prevTouchInterval = MIN_TOUCH_INTERVAL
            }
            interval > MAX_TOUCH_INTERVAL -> {
                //Если интервал нажатий слишком большой, сбрасываем последовательность
                isFirstClick = true
                prevTouchTime = System.currentTimeMillis()
                onTapClickedListener?.invoke(null)
                return super.performClick()
            }
            isFirstClick -> {
                prevTouchInterval = interval
                isFirstClick = false
            }
            else -> {
                prevTouchInterval = (prevTouchInterval + interval) / 2
            }
        }

        val bpm = ((60 * 1000) / prevTouchInterval).toInt()

        onTapClickedListener?.invoke(bpm)
        prevTouchTime = System.currentTimeMillis()
        return super.performClick()
    }

    private fun animateClick() {
        if (glowAnimator.isStarted) {
            glowAnimator.cancel()
        }
        glowAnimator.start()
    }

    companion object {
        private const val MIN_TOUCH_INTERVAL: Long = (60L * 1000L) / MAX_BPM
        private const val MAX_TOUCH_INTERVAL: Long = (60L * 1000L) / MIN_BPM

        private const val GLOW_RADIUS = 30f
        private const val MAX_GLOW_ALPHA = 0.6f
        private const val CIRCLE_BRIGHTNESS = 0.8f
    }
}