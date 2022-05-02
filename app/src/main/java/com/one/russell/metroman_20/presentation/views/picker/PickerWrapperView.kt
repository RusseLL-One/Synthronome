package com.one.russell.metroman_20.presentation.views.picker

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.withTranslation
import androidx.core.view.updatePadding
import com.one.russell.metroman_20.R
import com.one.russell.metroman_20.presentation.views.utils.GradientOrientation
import com.one.russell.metroman_20.presentation.views.utils.createGradientPaint
import com.one.russell.metroman_20.toPx
import com.shawnlin.numberpicker.NumberPicker

class PickerWrapperView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var frameThickness = 2.toPx()
    private var frameHeight = 45.toPx()
    private var frameWidth = 64.toPx()
    private var cornerRadius = 10.toPx()

    private var framePaint = Paint()

    val view = NumberPicker(context, attrs, defStyleAttr)

    init {
        setWillNotDraw(false)
        updatePadding(top = 7.toPx().toInt()) // for centering numbers
        view.apply {
            typeface = ResourcesCompat.getFont(context, R.font.poppins)
            setSelectedTypeface(ResourcesCompat.getFont(context, R.font.poppins))
            textColor = Color.WHITE
            selectedTextColor = Color.WHITE
            setDividerThickness(0)
            isBaselineAligned = true
        }.also { addView(it) }
    }

    fun setupPaints(@ColorInt startColor: Int, @ColorInt endColor: Int) = post {
        framePaint = createGradientPaint(
            gradientOrientation = GradientOrientation.BR_TL,
            width = frameWidth,
            height = frameHeight,
            startColor = startColor,
            endColor = endColor,
            alpha = 0.6f,
            strokeWidth = frameThickness
        )
        invalidate()
    }

    override fun onDrawForeground(canvas: Canvas) {
        super.onDrawForeground(canvas)
        canvas.drawFrame(framePaint)
    }

    private fun Canvas.drawFrame(paint: Paint) {
        withTranslation((width.toFloat() - frameWidth) / 2, (height.toFloat() - frameHeight) / 2) {
            drawRoundRect(0f, 0f, frameWidth, frameHeight, cornerRadius, cornerRadius, paint)
        }
    }
}