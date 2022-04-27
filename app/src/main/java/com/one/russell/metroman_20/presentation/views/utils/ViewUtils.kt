package com.one.russell.metroman_20.presentation.views.utils

import android.graphics.*
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.one.russell.metroman_20.presentation.views.utils.GradientOrientation.*

val View.centerX: Float
    get() = width.toFloat() / 2

val View.centerY: Float
    get() = height.toFloat() / 2

fun createGlowPaint(
    @ColorInt color: Int,
    thickness: Float,
    glowRadius: Float,
    @FloatRange(from = 0.0, to = 1.0) glowIntense: Float,
    style: Paint.Style = Paint.Style.STROKE
) = Paint().apply {
    this.isAntiAlias = true
    this.color = color
    this.maskFilter = BlurMaskFilter(
        glowRadius,
        BlurMaskFilter.Blur.NORMAL
    )
    this.strokeWidth = thickness + glowRadius
    this.style = style
    this.alpha = (glowIntense * 255).toInt()
}

fun createBevelPaint(
    centerX: Float,
    centerY: Float,
    radius: Float,
    @FloatRange(from = 0.0, to = 1.0) alpha: Float,
    @ColorInt startColor: Int,
    @ColorInt endColor: Int,
    strokeThickness: Float = 0f,
    style: Paint.Style = Paint.Style.STROKE
) = Paint().apply {
    this.isAntiAlias = true
    this.shader = LinearGradient(
        centerX - radius,
        centerY - radius,
        centerX + radius,
        centerY + radius,
        startColor,
        endColor,
        Shader.TileMode.MIRROR
    )
    this.strokeWidth = strokeThickness
    this.alpha = (alpha * 255).toInt()
    this.style = style
}

fun createGradientPaint(
    gradientOrientation: GradientOrientation,
    width: Float,
    height: Float,
    @ColorInt startColor: Int,
    @ColorInt endColor: Int,
    @FloatRange(from = 0.0, to = 1.0) alpha: Float,
    style: Paint.Style = Paint.Style.STROKE,
    strokeWidth: Float = 0f
) = Paint().apply {
    this.isAntiAlias = true
    this.shader = createLinearGradient(gradientOrientation, width, height, startColor, endColor)
    this.alpha = (alpha * 255).toInt()
    this.style = style
    this.strokeWidth = strokeWidth
}

fun createLinearGradient(
    gradientOrientation: GradientOrientation,
    width: Float,
    height: Float,
    @ColorInt startColor: Int,
    @ColorInt endColor: Int
): LinearGradient {
    val x0: Float
    val y0: Float
    val x1: Float
    val y1: Float
    when (gradientOrientation) {
        TOP_BOTTOM -> { x0 = 0f; y0 = 0f; x1 = 0f; y1 = height }
        TR_BL -> { x0 = width; y0 = 0f; x1 = 0f; y1 = height }
        RIGHT_LEFT -> { x0 = width; y0 = 0f; x1 = 0f; y1 = 0f }
        BR_TL -> { x0 = width; y0 = height; x1 = 0f; y1 = 0f }
        BOTTOM_TOP -> { x0 = 0f; y0 = height; x1 = 0f; y1 = 0f }
        BL_TR -> { x0 = 0f; y0 = 0f; x1 = width; y1 = height }
        LEFT_RIGHT -> { x0 = 0f; y0 = 0f; x1 = width; y1 = 0f }
        TL_BR -> { x0 = 0f; y0 = 0f; x1 = width; y1 = height }
    }
    return LinearGradient(x0, y0, x1, y1, startColor, endColor, Shader.TileMode.CLAMP)
}

fun createRadialGradient(
    radius: Float,
    @ColorInt centerColor: Int,
    @ColorInt edgeColor: Int
): RadialGradient {
    return RadialGradient(
        0f,
        0f,
        radius,
        centerColor,
        edgeColor,
        Shader.TileMode.CLAMP
    )
}

fun createPaint(
    @ColorInt color: Int,
    @FloatRange(from = 0.0, to = 1.0) brightness: Float,
    strokeThickness: Float = 0f,
    style: Paint.Style = Paint.Style.STROKE
) = Paint().apply {
    this.isAntiAlias = true
    this.color = Color.argb(
        Color.alpha(color),
        (Color.red(color) * brightness).toInt(),
        (Color.green(color) * brightness).toInt(),
        (Color.blue(color) * brightness).toInt()
    )
    this.strokeWidth = strokeThickness
    this.style = style
}

fun createPaddingsDecoration(horizontalPadding: Float = 0f, verticalPadding: Float = 0f): RecyclerView.ItemDecoration {
    return object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.left = (horizontalPadding / 2).toInt()
            outRect.right = (horizontalPadding / 2).toInt()
            outRect.top = (verticalPadding / 2).toInt()
            outRect.bottom = (verticalPadding / 2).toInt()
        }
    }
}

fun RecyclerView.executeAfterAllAnimationsAreFinished(
    callback: (RecyclerView) -> Unit
) = post(
    object : Runnable {
        override fun run() {
            if (isAnimating) {
                //if isAnimating() returned true itemAnimator will be non-null
                itemAnimator!!.isRunning {
                    post(this)
                }
            } else {
                callback(this@executeAfterAllAnimationsAreFinished)
            }
        }
    }
)

fun RecyclerView.disableScrolling() {
    layoutManager = object : LinearLayoutManager(context) {
        override fun canScrollVertically(): Boolean = false
        override fun canScrollHorizontally(): Boolean = false
    }
}