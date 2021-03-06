package com.one.russell.synthronome.presentation.views.color_indicator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import com.one.russell.synthronome.presentation.views.utils.createPaint
import com.one.russell.synthronome.toPx

class ColorIndicatorView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val borderThickness: Float = 2.toPx()

    private var colorPaint = Paint()
    private var borderPaint = Paint()

    fun setupPaints(@ColorInt borderColor: Int, @ColorInt colorPrimary: Int) = post {
        colorPaint = createPaint(
            color = colorPrimary,
            brightness = 1f,
            style = Paint.Style.FILL
        )
        borderPaint = createPaint(
            color = borderColor,
            brightness = 1f,
            strokeThickness = borderThickness
        )
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawCircle(colorPaint)
        canvas.drawCircle(borderPaint, borderThickness / 2)
        super.onDraw(canvas)
    }

    private fun Canvas.drawCircle(paint: Paint, offset: Float = 0f) {
        drawOval(
            offset,
            offset,
            width.toFloat() - offset,
            height.toFloat() - offset,
            paint
        )
    }
}
