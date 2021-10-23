package com.one.russell.metroman_20.presentation.views.rotary_knob

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs
import kotlin.math.atan2

abstract class KnobView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var onValueChangedCallbacks: ArrayList<(Int) -> Unit> = ArrayList(4)

    private var isBlocked = false

    // Amount of degrees in one step
    private var degreesInStep: Int = 10

    // Amount of degrees between touch point and current degrees
    private var offsetDegrees = 0f

    // Degrees at touch point
    private var touchDegrees = 0f

    // Amount of degrees the knob is rotated
    protected var currentDegrees = 0f

    fun addOnValueChangedCallback(callback: (Int) -> Unit) {
        onValueChangedCallbacks.add(callback)
    }

    fun setStepDegrees(stepDegrees: Int) {
        this.degreesInStep = stepDegrees
    }

    fun setIsBlocked(isBlocked: Boolean) {
        this.isBlocked = isBlocked
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)

        val size = when {
            widthMode == MeasureSpec.EXACTLY && widthSize > 0 && widthSize <= heightSize ->
                widthSize
            heightMode == MeasureSpec.EXACTLY && heightSize > 0 && heightSize <= widthSize ->
                heightSize
            widthSize < heightSize ->
                widthSize
            else ->
                heightSize
        }

        val finalMeasureSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY)
        super.onMeasure(finalMeasureSpec, finalMeasureSpec)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (isBlocked) return false

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                val startX = event.x / width
                val startY = event.y / height
                touchDegrees = cartesianToPolar(startX, startY)
                offsetDegrees = touchDegrees - currentDegrees
            }
            MotionEvent.ACTION_MOVE -> {
                val x = event.x / width
                val y = event.y / height
                val newDegrees = cartesianToPolar(x, y)
                val step = calcStep(touchDegrees, newDegrees)
                if (abs(step) >= 1) {
                    onValueChangedCallbacks.forEach { it.invoke(step) }
                }
                touchDegrees = newDegrees
                currentDegrees = touchDegrees - offsetDegrees
                invalidate()
            }
        }
        return true
    }

    private fun cartesianToPolar(x: Float, y: Float): Float {
        return 180 + (-Math.toDegrees(
            atan2(
                (x - 0.5f).toDouble(),
                (y - 0.5f).toDouble()
            )
        )).toFloat()
    }

    private fun calcStep(oldDegrees: Float, newDegrees: Float): Int {
        val oldDegreesSteps = (oldDegrees / degreesInStep).toInt()
        val newDegreesSteps = (newDegrees / degreesInStep).toInt()
        val stepsCount = (360 / degreesInStep)

        return when {
            newDegreesSteps == oldDegreesSteps -> 0
            newDegreesSteps > oldDegreesSteps -> {
                val clockwiseSteps = newDegreesSteps - oldDegreesSteps
                val counterClockwiseSteps = stepsCount - newDegreesSteps + oldDegreesSteps
                if (clockwiseSteps < counterClockwiseSteps) {
                    newDegreesSteps - oldDegreesSteps
                } else {
                    newDegreesSteps - oldDegreesSteps - stepsCount
                }
            }
            else -> {
                val clockwiseSteps = stepsCount - oldDegreesSteps + newDegreesSteps
                val counterClockwiseSteps = oldDegreesSteps - newDegreesSteps

                if (clockwiseSteps < counterClockwiseSteps) {
                    newDegreesSteps - oldDegreesSteps + stepsCount
                } else {
                    newDegreesSteps - oldDegreesSteps
                }
            }
        }
    }
}