package com.one.russell.metroman_20.presentation.views.beatline

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt
import androidx.annotation.IntRange
import androidx.core.content.res.getDimensionPixelSizeOrThrow
import androidx.core.graphics.withTranslation
import androidx.core.view.doOnLayout
import com.one.russell.metroman_20.R
import com.one.russell.metroman_20.getStyledAttributes
import com.one.russell.metroman_20.presentation.views.utils.*
import com.one.russell.metroman_20.toPx
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class BeatlineView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var radius: Int = 0

    private val ballRadius = 13.toPx()
    private val pointsRadius = 3.toPx()
    private val pointsGlowRadius = 7.toPx()

    private var ballPos = BallPosition(0f)
    private var pointsPos = listOf<Point>()
    private var ballPaint = Paint()
    private var pointsPaint = Paint()
    private var pointsGlowPaint = Paint()

    private var reverseAnimation = false
    private val animator = ValueAnimator
        .ofFloat(0f, 1f)
        .apply {
            interpolator = LinearInterpolator()
            addUpdateListener {
                ballPos.positionFactor = it.animatedValue as Float
                ballPos.calcBallCoords(ballPos.positionFactor)
                invalidate()
            }
        }

    init {
        initAttrs(context, attrs, defStyleAttr)

        doOnLayout {
            ballPos.calcBallCoords(0f)
            pointsPos = calcPointsPositions()
        }
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?, defStyle: Int) {
        context.getStyledAttributes(attrs, R.styleable.BeatlineView, defStyle) {
            radius = getDimensionPixelSizeOrThrow(R.styleable.BeatlineView_radius)
        }
    }

    fun setupPaints(@ColorInt colorPrimary: Int, @ColorInt colorSecondary: Int) = post {
        ballPaint = Paint().apply {
            isAntiAlias = true
            shader = createRadialGradient(ballRadius, colorSecondary, colorPrimary)
        }
        pointsPaint = createGradientPaint(
            gradientOrientation = GradientOrientation.BOTTOM_TOP,
            width = width.toFloat(),
            height = centerY + radius * sin(Math.toRadians(START_POINT.toDouble())).toFloat(),
            startColor = colorPrimary,
            endColor = colorSecondary,
            alpha = 1f,
            style = Paint.Style.FILL
        )
        pointsGlowPaint = Paint(pointsPaint).apply {
            maskFilter = BlurMaskFilter(pointsGlowRadius, BlurMaskFilter.Blur.NORMAL)
        }
        invalidate()
    }

    private fun calcPointsPositions(): List<Point> {
        return List(DOTS_COUNT) {
            Point().apply { calcBallCoords(it.toFloat() / (DOTS_COUNT - 1)) }
        }
    }

    private fun Point.calcBallCoords(animValue: Float) {
        val angleDeg: Double = (animValue * AMPLITUDE + START_POINT).toDouble()
        val angleRad = Math.toRadians(angleDeg)

        x = (centerX + radius * cos(angleRad)).toInt()
        y = (centerY + radius * sin(angleRad)).toInt()
    }

    fun animateBall(bpm: Int) {
        if (animator.isStarted) {
            animator.end()
        }
        animator.run {
            duration = ((60f / bpm) * 1000).toLong()
            if (reverseAnimation) reverse() else start()
            reverseAnimation = !reverseAnimation
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = 2 * (radius + ballRadius).toInt()

        val finalMeasureSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY)
        super.onMeasure(finalMeasureSpec, finalMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        pointsPos.forEachIndexed { index, point ->
            pointsPaint.alpha = calcPointAlpha(index)
            pointsGlowPaint.alpha = pointsPaint.alpha
            canvas.drawCircle(point.x.toFloat(), point.y.toFloat(), pointsRadius, pointsPaint)
            canvas.drawCircle(point.x.toFloat(), point.y.toFloat(), pointsGlowRadius, pointsGlowPaint)
        }

        canvas.withTranslation(ballPos.x.toFloat(), ballPos.y.toFloat()) {
            canvas.drawCircle(0f, 0f, ballRadius, ballPaint)
        }
    }

    @IntRange(from = 0, to = 255)
    private fun calcPointAlpha(index: Int): Int {
        val pointPositionFactor = index.toFloat() / (DOTS_COUNT - 1)
        val ballProximityFactor = 1 - abs(pointPositionFactor - ballPos.positionFactor)
        val visibleAreaFactor = 0.7f
        val alpha = (ballProximityFactor + visibleAreaFactor - 1).coerceAtLeast(0f)
        return (alpha * 255).toInt()
    }

    data class BallPosition(
        var positionFactor: Float
    ) : Point()

    companion object {
        private const val AMPLITUDE = 180f
        private const val START_POINT = -90f - AMPLITUDE / 2 // So that the top point is the center

        private const val DOTS_COUNT = 18
    }
}