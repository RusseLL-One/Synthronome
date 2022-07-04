package com.one.russell.synthronome.presentation.views.training_overlay

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.one.russell.synthronome.R
import com.one.russell.synthronome.databinding.ViewTrainingOverlayBinding
import com.one.russell.synthronome.presentation.views.utils.GradientOrientation
import com.one.russell.synthronome.presentation.views.utils.createGradientPaint

class TrainingOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = ViewTrainingOverlayBinding.inflate(LayoutInflater.from(context), this)

    private var bgPaint: Paint = Paint()

    init {
        setWillNotDraw(false)
        isClickable = true
    }

    fun setupPaints(@ColorInt colorPrimary: Int, @ColorInt colorSecondary: Int, @ColorInt textColor: Int) = post {
        bgPaint = createGradientPaint(
            GradientOrientation.TOP_BOTTOM,
            width.toFloat(),
            height.toFloat(),
            colorPrimary,
            colorSecondary,
            1f,
            Paint.Style.FILL,
        )

        binding.tvTrainingInProgressTitle.setTextColor(textColor)
        binding.tvTrainingCompletion.setTextColor(textColor)

        invalidate()
    }

    fun setPercentageVisible(isVisible: Boolean) {
        binding.tvTrainingCompletion.isVisible = isVisible
    }

    fun setPercent(percent: Int) {
        binding.tvTrainingCompletion.text =
                context.getString(R.string.training_completion_percent, percent)
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)
    }
}