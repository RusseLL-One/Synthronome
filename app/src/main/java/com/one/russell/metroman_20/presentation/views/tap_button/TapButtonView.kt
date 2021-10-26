package com.one.russell.metroman_20.presentation.views.tap_button

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.one.russell.metroman_20.domain.Constants.MAX_BPM
import com.one.russell.metroman_20.domain.Constants.MIN_BPM

class TapButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var onTapClickedListener: ((Int?) -> Unit)? = null

    private var prevTouchTime = 0L
    private var prevTouchInterval = 0L
    private var isFirstClick = true

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

    companion object {
        private const val MIN_TOUCH_INTERVAL: Long = (60L * 1000L) / MAX_BPM
        private const val MAX_TOUCH_INTERVAL: Long = (60L * 1000L) / MIN_BPM
    }
}