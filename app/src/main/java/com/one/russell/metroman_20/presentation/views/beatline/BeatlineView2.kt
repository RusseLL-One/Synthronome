package com.one.russell.metroman_20.presentation.views.beatline

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.CycleInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import androidx.core.content.ContextCompat
import androidx.core.view.doOnLayout
import com.one.russell.metroman_20.R
import com.one.russell.metroman_20.presentation.views.utils.centerX

class BeatlineView2 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var pointPos = Point()
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = ContextCompat.getColor(context, R.color.primary) }
    private var linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 3f
        color = ContextCompat.getColor(context, R.color.cardview_dark_background)
    }

    private val animator = ValueAnimator
        .ofFloat(0f, 1f)
        .apply {
            interpolator = CycleInterpolator(0.5f)
            addUpdateListener {
                pointPos.calcBallCoords(it.animatedValue as Float)
                invalidate()
            }
        }

    init {
        doOnLayout {
            pointPos.calcBallCoords(0f)
        }
    }

    private fun Point.calcBallCoords(animatedValue: Float) {
        x = centerX.toInt()
        y = (height - (AMPLITUDE * animatedValue)).toInt()
    }

    fun animateBall(bpm: Int) {
        if (animator.isRunning) {
            animator.cancel()
        }
        animator.run {
            duration = ((60f / bpm) * 1000).toLong()
            start()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = (2 * BALL_RADIUS).toInt()
        val heightSize = (2 * BALL_RADIUS + AMPLITUDE).toInt()

        val finalWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY)
        val finalHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY)
        super.onMeasure(finalWidthMeasureSpec, finalHeightMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawLine(
            centerX,
            height.toFloat(),
            centerX,
            height.toFloat() - AMPLITUDE,
            linePaint
        )
        canvas?.drawCircle(pointPos.x.toFloat(), pointPos.y.toFloat(), BALL_RADIUS, paint)
    }

    companion object {
        private const val AMPLITUDE = 200f
        private const val START_POINT = -90f - AMPLITUDE / 2 // So that the top point is the center
        private const val BALL_RADIUS = 40f
    }
}
