package com.one.russell.metroman_20.presentation.views.bookmark

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.withClip
import com.one.russell.metroman_20.R
import com.one.russell.metroman_20.getColorCompat
import com.one.russell.metroman_20.presentation.views.utils.GradientOrientation
import com.one.russell.metroman_20.presentation.views.utils.createGradientPaint
import com.one.russell.metroman_20.presentation.views.utils.createPaint
import com.one.russell.metroman_20.toPx

class BookmarkCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    private val borderThickness: Float = 2.toPx()
    private val cornerRadius: Float = 13.toPx()
    private val removeButtonWidth: Float = 35.toPx()

    private val removeIcon: Drawable? by lazy(LazyThreadSafetyMode.NONE) {
        getRemoveButtonDrawable(R.drawable.ic_remove)
    }

    private var bgPaint = Paint()
    private var borderPaint = Paint()
    private var colorfulBorderPaint = Paint()
    private var removeButtonPaint = Paint()
    private var removeButtonClipPath = Path()

    private val borderColorAnimator = ValueAnimator()
        .apply {
            duration = 160
            addUpdateListener {
                colorfulBorderPaint.alpha = ((it.animatedValue as Float) * 255).toInt()
                invalidate()
            }
        }

    private var onRemoveButtonClickListener: (() -> Unit)? = null
    private var isToggled = false

    private fun getRemoveButtonDrawable(@DrawableRes drawableRes: Int): Drawable? {
        return ResourcesCompat.getDrawable(resources, drawableRes, context.theme)
            ?.apply {
                val left = (width - (removeButtonWidth + intrinsicWidth) / 2).toInt()
                val top = (height - intrinsicHeight) / 2
                bounds = Rect(left, top, left + intrinsicWidth, top + intrinsicHeight)
            }
    }

    fun setupPaints(@ColorInt colorPrimary: Int, @ColorInt colorSecondary: Int) = post {
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
            startColor = colorPrimary,
            endColor = colorSecondary,
            alpha = if (isToggled) 1f else 0f,
            strokeWidth = borderThickness,
        )
        removeButtonPaint = createPaint(
            color = context.getColorCompat(R.color.remove_bookmark_color),
            brightness = 1f,
            style = Paint.Style.FILL
        )
        removeButtonClipPath = Path().apply {
            fillType = Path.FillType.INVERSE_WINDING
            addRoundRect(
                0f,
                0f,
                width.toFloat() - removeButtonWidth,
                height.toFloat(),
                cornerRadius, cornerRadius,
                Path.Direction.CW
            )
        }
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.withClip(removeButtonClipPath) {
            drawRemoveButton(removeButtonPaint)
        }
        canvas.drawBackground(bgPaint)
        canvas.drawBorder(borderPaint)
        canvas.drawBorder(colorfulBorderPaint)
        removeIcon?.draw(canvas)
        super.onDraw(canvas)
    }

    private fun Canvas.drawRemoveButton(paint: Paint) {
        drawRoundRect(
            width.toFloat() - removeButtonWidth - cornerRadius * 2,
            borderThickness,
            width.toFloat(),
            height.toFloat() - borderThickness,
            cornerRadius,
            cornerRadius,
            paint
        )
    }

    private fun Canvas.drawBackground(paint: Paint) {
        drawRoundRect(
            0f,
            0f,
            width.toFloat() - removeButtonWidth,
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
            width.toFloat() - offset - removeButtonWidth,
            height.toFloat() - offset,
            cornerRadius - offset,
            cornerRadius - offset,
            paint
        )
    }

    fun setOnRemoveButtonClickListener(onRemoveButtonClickListener: (() -> Unit)) {
        this.onRemoveButtonClickListener = onRemoveButtonClickListener
    }

    fun setToggled(isToggled: Boolean, animate: Boolean) {
        if (animate) {
            animateButton(isToggled)
        } else {
            colorfulBorderPaint.alpha = if (isToggled) 255 else 0
            invalidate()
        }

        this.isToggled = isToggled
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        fun isInsideCard(): Boolean {
            return Rect(0, 0, width - removeButtonWidth.toInt(), height)
                .contains(event.x.toInt(), event.y.toInt())
        }

        fun isInsideRemoveButton(): Boolean {
            return Rect(width - removeButtonWidth.toInt(), 0, width, height)
                .contains(event.x.toInt(), event.y.toInt())
        }

        when (event.action) {
            MotionEvent.ACTION_UP -> {
                when {
                    isInsideCard() -> {
                        performClick()
                        return true
                    }
                    isInsideRemoveButton() -> {
                        onRemoveButtonClickListener?.invoke()
                        return true
                    }
                }
                return false
            }
        }
        return super.onTouchEvent(event)
    }

    private fun animateButton(isToggled: Boolean) {
        if (borderColorAnimator.isStarted) {
            borderColorAnimator.reverse()
        } else {
            borderColorAnimator.run {
                if (isToggled) setFloatValues(0f, 1f)
                else setFloatValues(1f, 0f)
                start()
            }
        }
    }
}