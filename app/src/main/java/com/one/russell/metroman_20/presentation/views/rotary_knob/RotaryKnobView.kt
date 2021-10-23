package com.one.russell.metroman_20.presentation.views.rotary_knob

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.RectShape
import android.util.AttributeSet
import android.view.HapticFeedbackConstants
import androidx.core.content.ContextCompat
import androidx.core.graphics.withRotation
import androidx.core.view.doOnLayout
import com.one.russell.metroman_20.R

class RotaryKnobView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : KnobView(context, attrs, defStyleAttr) {

    private val centerX: Float
        get() = width.toFloat() / 2
    private val centerY: Float
        get() = height.toFloat() / 2

    private val primaryColor: Int = ContextCompat.getColor(context, R.color.primary_orange)

    private var ringDrawable: GlowingRing? = null
    private var dashDrawable: Drawable? = null

    init {
        isHapticFeedbackEnabled = true
        addOnValueChangedCallback {
            performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
        }

        doOnLayout {
            initDrawables()
        }
    }

    private fun initDrawables() {
        ringDrawable = GlowingRing(context, width, height, primaryColor)

        dashDrawable = ShapeDrawable(RectShape()).apply {
            paint.color = primaryColor
            setBounds(
                centerX.toInt() - 6, // todo
                centerY.toInt() - 300,
                centerX.toInt() + 6,
                centerY.toInt() - 250
            )
        }
    }

    override fun onDraw(canvas: Canvas) {
        ringDrawable?.draw(canvas)
        canvas.withRotation(degrees = currentDegrees, pivotX = centerX, pivotY = centerY) {
            dashDrawable?.draw(canvas)
        }

        super.onDraw(canvas)
    }

    fun setGlowIntense(intense: Float) {
        ringDrawable?.setGlowIntense(intense)
    }
}