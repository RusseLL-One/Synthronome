package com.one.russell.metroman_20.presentation.views.rotary_knob

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.annotation.ColorInt
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.doOnLayout
import com.one.russell.metroman_20.R
import com.one.russell.metroman_20.getColorCompat
import com.one.russell.metroman_20.getStyledAttributes
import com.one.russell.metroman_20.presentation.views.utils.*
import com.one.russell.metroman_20.toPx

class ButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    private var image: Drawable? = null

    private var bgPaint = Paint()
    private var borderPaint = Paint()
    private var colorfulBorderPaint = Paint()

    private val cornerRadius: Float = 13.toPx()
    private val borderThickness: Float = 2.toPx()

    private val borderColorAnimator = ValueAnimator()
        .apply {
            duration = 70
            addUpdateListener {
                colorfulBorderPaint.alpha = ((it.animatedValue as Float) * 255).toInt()
                invalidate()
            }
        }

    init {
        initAttrs(context, attrs, defStyleAttr)

        doOnLayout {
            image?.apply { bounds = getDrawableBounds(this) }
        }
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?, defStyle: Int) {
        context.getStyledAttributes(attrs, R.styleable.ButtonView, defStyle) {
            image = getResourceId(R.styleable.ButtonView_image, 0)
                .takeIf { it != 0 }
                ?.let { AppCompatResources.getDrawable(context, it) }
        }
    }

    fun setupPaints(@ColorInt primaryColor: Int, @ColorInt secondaryColor: Int) = post {
        bgPaint = createGradientPaint(
            gradientOrientation = GradientOrientation.TL_BR,
            width = width.toFloat(),
            height = height.toFloat(),
            startColor = context.getColorCompat(R.color.view_bg_gradient_color_1),
            endColor = context.getColorCompat(R.color.view_bg_gradient_color_2),
            alpha = 1f,
            style = Paint.Style.FILL
        )
        borderPaint = createGradientPaint(
            gradientOrientation = GradientOrientation.TL_BR,
            width = width.toFloat(),
            height = height.toFloat(),
            startColor = context.getColorCompat(R.color.border_gradient_color_1),
            endColor = context.getColorCompat(R.color.border_gradient_color_2),
            alpha = 1f,
            strokeWidth = borderThickness
        )
        colorfulBorderPaint = createGradientPaint(
            gradientOrientation = GradientOrientation.LEFT_RIGHT,
            width = width.toFloat(),
            height = height.toFloat(),
            startColor = primaryColor,
            endColor = secondaryColor,
            alpha = 0f,
            strokeWidth = borderThickness,
        )
        invalidate()
    }

    private fun getDrawableBounds(drawable: Drawable): Rect {
        return Rect(
            (width - drawable.intrinsicWidth) / 2,
            (height - drawable.intrinsicHeight) / 2,
            (width + drawable.intrinsicWidth) / 2,
            (height + drawable.intrinsicHeight) / 2
        )
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawBackground(bgPaint)
        canvas.drawBorder(borderPaint)
        canvas.drawBorder(colorfulBorderPaint)
        image?.draw(canvas)
        super.onDraw(canvas)
    }

    private fun Canvas.drawBackground(paint: Paint) {
        drawRoundRect(
            0f,
            0f,
            width.toFloat(),
            height.toFloat(),
            cornerRadius,
            cornerRadius,
            paint
        )
    }

    private fun Canvas.drawBorder(paint: Paint) {
        val offset = borderThickness / 2
        drawRoundRect(
            offset,
            offset,
            width.toFloat() - offset,
            height.toFloat() - offset,
            cornerRadius - offset,
            cornerRadius - offset,
            paint
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        fun isInside(): Boolean {
            return Rect()
                .apply { getLocalVisibleRect(this) }
                .contains(event.x.toInt(), event.y.toInt())
        }

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                animateButton(true)
            }
            MotionEvent.ACTION_UP -> {
                animateButton(false)
                if (isInside()) performClick()
            }
        }
        return super.onTouchEvent(event)
    }

    private fun animateButton(isPressed: Boolean) {
        if (borderColorAnimator.isStarted) {
            borderColorAnimator.reverse()
        } else {
            borderColorAnimator.run {
                if (isPressed) setFloatValues(0f, 1f)
                else setFloatValues(1f, 0f)
                start()
            }
        }
    }
}