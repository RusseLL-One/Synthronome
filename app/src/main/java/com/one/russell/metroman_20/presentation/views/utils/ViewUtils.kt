package com.one.russell.metroman_20.presentation.views.utils

import android.graphics.*
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange

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