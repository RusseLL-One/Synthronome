package com.one.russell.synthronome.presentation.views.buttons

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import com.one.russell.synthronome.R
import com.one.russell.synthronome.presentation.views.utils.GradientOrientation
import com.one.russell.synthronome.presentation.views.utils.createGradientPaint

class StartButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var bgPaint: Paint = Paint()

    private var playDrawable: Drawable? = null
    private var stopDrawable: Drawable? = null

    private var isButtonPressed = false

    fun setupPaints(@ColorInt startColor: Int, @ColorInt endColor: Int,  @ColorInt drawableTint: Int) = post {
        bgPaint = createGradientPaint(
            gradientOrientation = GradientOrientation.BOTTOM_TOP,
            width = width.toFloat(),
            height = height.toFloat(),
            startColor = startColor,
            endColor = endColor,
            alpha = 1f,
            style = Paint.Style.FILL,
        )

        playDrawable = getButtonDrawable(R.drawable.ic_play, drawableTint)
        stopDrawable = getButtonDrawable(R.drawable.ic_stop, drawableTint)

        invalidate()
    }

    private fun getButtonDrawable(@DrawableRes drawableRes: Int, @ColorInt tint: Int): Drawable? {
        return ResourcesCompat.getDrawable(resources, drawableRes, context.theme)
            ?.apply {
                bounds = getDrawableBounds(this)
                setTint(tint)
            }
    }

    private fun getDrawableBounds(drawable: Drawable): Rect {
        val left = (width - drawable.intrinsicWidth) / 2
        val top = (height - drawable.intrinsicHeight) / 2
        return Rect(
            left,
            top,
            left + drawable.intrinsicWidth,
            top + drawable.intrinsicHeight
        )
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawOval(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        if (isButtonPressed) {
            stopDrawable?.draw(canvas)
        } else {
            playDrawable?.draw(canvas)
        }
    }

    fun setButtonPressed(isButtonPressed: Boolean) {
        this.isButtonPressed = isButtonPressed
        invalidate()
    }
}