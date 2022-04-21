package com.one.russell.metroman_20.presentation.views.beatline

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Point
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.ContextCompat
import androidx.core.content.res.getDimensionPixelSizeOrThrow
import androidx.core.view.doOnLayout
import com.one.russell.metroman_20.R
import com.one.russell.metroman_20.getStyledAttributes
import com.one.russell.metroman_20.presentation.views.utils.centerX
import com.one.russell.metroman_20.presentation.views.utils.centerY
import kotlin.math.cos
import kotlin.math.sin

class BeatlineView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var radius: Int = 0

    private var pointPos = Point()
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { color = ContextCompat.getColor(context, R.color.primary) }
    private var linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 3f
        color = ContextCompat.getColor(context, R.color.cardview_dark_background)
    }

    private var reverseAnimation = false
    private val animator = ValueAnimator
        .ofFloat(0f, 1f)
        .apply {
            interpolator = LinearInterpolator()
            addUpdateListener {
                pointPos.calcBallCoords(it.animatedValue as Float)
                invalidate()
            }
        }

    init {
        initAttrs(context, attrs, defStyleAttr)

        doOnLayout {
            pointPos.calcBallCoords(0f)
        }
    }

    private fun Point.calcBallCoords(animatedValue: Float) {
        val angleDeg: Double = (animatedValue * AMPLITUDE + START_POINT).toDouble()
        val angleRad = Math.toRadians(angleDeg)

        x = (centerX + radius * cos(angleRad)).toInt()
        y = (centerY + radius * sin(angleRad)).toInt()
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?, defStyle: Int) {
        context.getStyledAttributes(attrs, R.styleable.BeatlineView, defStyle) {
            radius = getDimensionPixelSizeOrThrow(R.styleable.BeatlineView_radius)
        }
    }

    fun animateBall(bpm: Int) {
        if (animator.isRunning) {
            animator.cancel()
        }
        animator.run {
            duration = ((60f / bpm) * 1000).toLong()
            if (reverseAnimation) reverse() else start()
            reverseAnimation = !reverseAnimation
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = 2 * (radius + BALL_RADIUS).toInt()

        val finalMeasureSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY)
        super.onMeasure(finalMeasureSpec, finalMeasureSpec)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        canvas?.drawArc(
            (centerX - radius),
            (centerY - radius),
            (centerX + radius),
            (centerY + radius),
            START_POINT,
            AMPLITUDE,
            false,
            linePaint
        )
        canvas?.drawCircle(pointPos.x.toFloat(), pointPos.y.toFloat(), BALL_RADIUS, paint)
    }

    companion object {
        private const val AMPLITUDE = 135f
        private const val START_POINT = -90f - AMPLITUDE / 2 // So that the top point is the center
        private const val BALL_RADIUS = 40f
    }
}