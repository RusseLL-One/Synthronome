package com.one.russell.synthronome.presentation.views.buttons

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatButton
import com.one.russell.synthronome.R
import com.one.russell.synthronome.getColorCompat
import com.one.russell.synthronome.getStyledAttributes
import com.one.russell.synthronome.presentation.views.utils.*
import com.one.russell.synthronome.toPx

class ButtonView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatButton(context, attrs, defStyleAttr) {

    private val borderThickness: Float = 2.toPx()

    private var isPressCanceled = false

    private var image: Drawable? = null
    private var cornerRadius: Float = 13.toPx()
    private var performClickOnActionDown: Boolean = false

    private var bgPaint = Paint()
    private var borderPaint = Paint()
    private var colorfulBorderPaint = Paint()

    @ColorInt private var colorPrimary: Int = 0
    @ColorInt private var colorSecondary: Int = 0
    @ColorInt private var contentTint: Int? = null

    private val borderColorAnimator = ValueAnimator()
        .apply {
            duration = 80
            addUpdateListener {
                colorfulBorderPaint.alpha = ((it.animatedValue as Float) * 255).toInt()
                invalidate()
            }
        }

    var isLocked = false
        set(value) {
            field = value
            setupPaints()
        }

    init {
        initAttrs(context, attrs, defStyleAttr)
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?, defStyle: Int) {
        context.getStyledAttributes(attrs, R.styleable.ButtonView, defStyle) {
            image = getResourceId(R.styleable.ButtonView_image, 0)
                .takeIf { it != 0 }
                ?.let { AppCompatResources.getDrawable(context, it) }

            cornerRadius = getDimension(R.styleable.ButtonView_cornerRadius, cornerRadius)
            performClickOnActionDown = getBoolean(R.styleable.ButtonView_performClickOnActionDown, performClickOnActionDown)
        }
    }

    fun setupColors(@ColorInt colorPrimary: Int, @ColorInt colorSecondary: Int, @ColorInt contentTint: Int) = post {
        this.colorPrimary = colorPrimary
        this.colorSecondary = colorSecondary
        this.contentTint = contentTint
        setupPaints()
    }

    private fun setupPaints() {
        val borderPaintStartColor = context.getColorCompat(
            if (!isLocked) R.color.border_gradient_color_1
            else R.color.blocked_view_color_1
        )
        val borderPaintEndColor = context.getColorCompat(
            if (!isLocked) R.color.border_gradient_color_2
            else R.color.blocked_view_color_2
        )

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
            startColor = borderPaintStartColor,
            endColor = borderPaintEndColor,
            alpha = 1f,
            strokeWidth = borderThickness
        )
        colorfulBorderPaint = createGradientPaint(
            gradientOrientation = GradientOrientation.LEFT_RIGHT,
            width = width.toFloat(),
            height = height.toFloat(),
            startColor = colorPrimary,
            endColor = colorSecondary,
            alpha = 0f,
            strokeWidth = borderThickness,
        )

        image?.apply { bounds = getDrawableBounds(this) }

        applyTint()
        invalidate()
    }

    private fun applyTint() {
        val contentTintColor = if (!isLocked) contentTint
        else context.getColorCompat(R.color.blocked_view_color_2)

        contentTintColor?.let {
            setTextColor(it)
            image?.setTint(it)
        }
    }

    private fun getDrawableBounds(drawable: Drawable): Rect {
        return Rect(
            (width - drawable.intrinsicWidth) / 2,
            (height - drawable.intrinsicHeight) / 2,
            (width + drawable.intrinsicWidth) / 2,
            (height + drawable.intrinsicHeight) / 2
        )
    }

    fun setImage(@DrawableRes image: Int) {
        this.image = AppCompatResources.getDrawable(context, image)
            ?.apply { bounds = getDrawableBounds(this) }

        applyTint()
        invalidate()
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

        if (isLocked) return false

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isPressCanceled = false
                animateButton(true)
                if (performClickOnActionDown) performClick()
                return true
            }
            MotionEvent.ACTION_UP -> {
                if (isPressCanceled) return false
                animateButton(false)
                if (!performClickOnActionDown) performClick()
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                if (isPressCanceled) return false
                isPressCanceled = !isInside()
                if (isPressCanceled) animateButton(false)
            }
            MotionEvent.ACTION_CANCEL -> {
                isPressCanceled = true
                animateButton(false)
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