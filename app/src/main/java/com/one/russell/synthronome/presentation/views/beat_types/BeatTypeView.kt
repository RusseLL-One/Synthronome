package com.one.russell.synthronome.presentation.views.beat_types

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.ColorInt
import androidx.core.graphics.withClip
import com.one.russell.synthronome.domain.BeatType
import com.one.russell.synthronome.presentation.views.utils.GradientOrientation
import com.one.russell.synthronome.presentation.views.utils.createGradientPaint
import com.one.russell.synthronome.toPx

class BeatTypeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val borderWidth = 2.toPx()
    private val offsetWidth = 3.toPx()
    private val lineWidth = 1.toPx()
    private val cornerRadius = 10.toPx()

    private var borderPaint: Paint = Paint()
    private var linesPaint: Paint = Paint()
    private var offsetPath: Path = Path()

    private val animator = ValueAnimator().apply {
        addUpdateListener {
            interpolator = AccelerateDecelerateInterpolator()
            linesCount = it.animatedValue as Int
            invalidate()
        }
    }

    private var beatType: BeatType = BeatType.BEAT
    private var linesCount = 0

    fun setupPaints(@ColorInt startColor: Int, @ColorInt endColor: Int) = post {
        borderPaint = createGradientPaint(
            gradientOrientation = GradientOrientation.BR_TL,
            width = width.toFloat(),
            height = height.toFloat(),
            startColor = startColor,
            endColor = endColor,
            alpha = 0.6f,
            strokeWidth = borderWidth
        )

        linesPaint = Paint(borderPaint).apply { strokeWidth = lineWidth }

        offsetPath = Path().apply {
            addRoundRect(
                getRectWithOffset(borderWidth + offsetWidth),
                getRoundingWithOffset(borderWidth + offsetWidth),
                getRoundingWithOffset(borderWidth + offsetWidth),
                Path.Direction.CW
            )
        }
        invalidate()
    }

    private fun getRectWithOffset(bordersOffset: Float): RectF {
        return RectF(
            bordersOffset,
            bordersOffset,
            width.toFloat() - bordersOffset,
            height.toFloat() - bordersOffset,
        )
    }

    private fun getRoundingWithOffset(bordersOffset: Float): Float {
        return cornerRadius - bordersOffset
    }

    override fun onDraw(canvas: Canvas) {
        canvas.withClip(offsetPath) {
            for (i in 0 until linesCount) {
                linesPaint.drawLine(canvas, i)
            }
        }

        borderPaint.drawBorder(canvas, borderWidth / 2)
    }

    private fun Paint.drawBorder(canvas: Canvas, bordersOffset: Float) {
        val rounding = getRoundingWithOffset(bordersOffset)
        canvas.drawRoundRect(getRectWithOffset(bordersOffset), rounding, rounding, this)
    }

    private fun Paint.drawLine(canvas: Canvas, lineIndex: Int) {
        val lineYPos = getLineYPosition(lineIndex)
        canvas.drawLine(0f, lineYPos, width.toFloat(), lineYPos, this)
    }

    private fun getLineYPosition(index: Int): Float {
        val heightOffset = borderWidth + offsetWidth + lineWidth / 2
        val availableHeight = height - heightOffset * 2
        val linePosition = (availableHeight / (TOTAL_LINES_COUNT - 1)) * index
        return height - heightOffset - linePosition
    }

    fun setBeatType(newBeatType: BeatType, animate: Boolean) {
        if (animate) {
            animateBeatTypeChange(newBeatType)
        } else {
            this.linesCount = getLineCount(newBeatType)
            invalidate()
        }

        this.beatType = newBeatType
    }

    private fun animateBeatTypeChange(newBeatType: BeatType) {
        if (animator.isStarted) {
            animator.end()
        }

        animator.setIntValues(getLineCount(beatType), getLineCount(newBeatType))
        animator.start()
    }

    private fun getLineCount(beatType: BeatType): Int {
        val linesCount = TOTAL_LINES_COUNT.toFloat() * when (beatType) {
            BeatType.MUTE -> 0f
            BeatType.BEAT -> 0.33f
            BeatType.SUBACCENT -> 0.66f
            BeatType.ACCENT -> 1f
        }
        return linesCount.toInt()
    }

    companion object {
        private const val TOTAL_LINES_COUNT = 25
    }
}