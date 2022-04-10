package com.one.russell.metroman_20.presentation.views.rotary_knob

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import androidx.core.graphics.withRotation
import androidx.core.view.doOnLayout
import com.one.russell.metroman_20.R
import com.one.russell.metroman_20.presentation.views.utils.centerX
import com.one.russell.metroman_20.presentation.views.utils.centerY
import com.one.russell.metroman_20.presentation.views.utils.createBevelPaint
import com.one.russell.metroman_20.presentation.views.utils.createPaint

class StartButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var outerBevelPaint: Paint? = null
    private var buttonPaint: Paint? = null
    private var symbolPaint: Paint? = null

    private val symbolRect = Rect()
    private val playPath = Path()
    private val stopPath = Path()
    private var isButtonPressed = false

    init {
        doOnLayout {
            initPaints(ContextCompat.getColor(context, R.color.primary))
        }
    }

    private fun initPaints(@ColorInt initColor: Int) {
        stopPath.moveTo(centerX - 30, centerY - 30)
        stopPath.lineTo(centerX + 30, centerY - 30)
        stopPath.lineTo(centerX + 30, centerY + 30)
        stopPath.lineTo(centerX - 30, centerY + 30)
        stopPath.close()

        playPath.moveTo(centerX - 30, centerY - 30)
        playPath.lineTo(centerX + 30, centerY)
        playPath.lineTo(centerX - 30, centerY + 30)
        playPath.close()

        symbolRect.set(
            (centerX - 30).toInt(),
            (centerY - 30).toInt(),
            (centerX + 30).toInt(),
            (centerY + 30).toInt()
        )

        outerBevelPaint = createBevelPaint(centerX, centerY, centerX - OUTER_BEVEL_THICKNESS_PX / 2, BEVEL_ALPHA, Color.WHITE, Color.BLACK, OUTER_BEVEL_THICKNESS_PX)
        buttonPaint = createBevelPaint(centerX, centerY, centerX - OUTER_BEVEL_THICKNESS_PX, BEVEL_ALPHA, Color.WHITE, Color.BLACK, style = Paint.Style.FILL)
        symbolPaint = createPaint(initColor, 1f, style = Paint.Style.FILL)
    }

    override fun onDraw(canvas: Canvas) {
        outerBevelPaint?.drawButton(canvas, centerX - OUTER_BEVEL_THICKNESS_PX / 2)
        canvas.withRotation(
            degrees = if (isButtonPressed) 180f else 0f,
            pivotX = centerX,
            pivotY = centerY
        ) {
            buttonPaint?.drawButton(canvas, centerX - OUTER_BEVEL_THICKNESS_PX)
        }

        symbolPaint?.let {
            if (isButtonPressed) {
                canvas.drawPath(stopPath, it)
            } else {
                canvas.drawPath(playPath, it)
            }
        }
    }

    private fun Paint.drawButton(canvas: Canvas, radius: Float) {
        canvas.drawCircle(
            centerX,
            centerY,
            radius,
            this
        )
    }

    fun setButtonPressed(isButtonPressed: Boolean) {
        this.isButtonPressed = isButtonPressed
        invalidate()
    }

    companion object {
        private const val OUTER_BEVEL_THICKNESS_PX = 8f
        private const val BEVEL_ALPHA = 0.7f
    }
}