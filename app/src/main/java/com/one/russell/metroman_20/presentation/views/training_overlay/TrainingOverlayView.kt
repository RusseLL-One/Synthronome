package com.one.russell.metroman_20.presentation.views.training_overlay

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.LinearInterpolator
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.one.russell.metroman_20.R
import com.one.russell.metroman_20.databinding.ViewTrainingOverlayBinding
import com.one.russell.metroman_20.domain.TrainingProcessor
import com.one.russell.metroman_20.domain.TrainingState
import com.one.russell.metroman_20.presentation.views.utils.GradientOrientation
import com.one.russell.metroman_20.presentation.views.utils.createGradientPaint

class TrainingOverlayView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private val binding = ViewTrainingOverlayBinding.inflate(LayoutInflater.from(context), this)
    private val percentAnimator = ValueAnimator
        .ofInt(0, 100)
        .apply {
            interpolator = LinearInterpolator()
            addUpdateListener {
                val percent = it.animatedValue as Int
                binding.tvTrainingCompletion.text =
                    context.getString(R.string.training_completion_percent, percent)
            }
        }

    private var bgPaint: Paint = Paint()

    init {
        setWillNotDraw(false)
        isClickable = true
    }

    fun setupPaints(@ColorInt primaryColor: Int, @ColorInt secondaryColor: Int) = post {
        bgPaint = createGradientPaint(
            GradientOrientation.TOP_BOTTOM,
            width.toFloat(),
            height.toFloat(),
            primaryColor,
            secondaryColor,
            1f,
            Paint.Style.FILL,
        )
        invalidate()
    }

    fun showTrainingCompletion(trainingState: TrainingState.Running) {
        if (percentAnimator.isStarted) {
            percentAnimator.end()
        }

        if (trainingState.durationMs == TrainingProcessor.TRAINING_TIME_INFINITE) {
            binding.tvTrainingCompletion.isVisible = false
        } else {
            binding.tvTrainingCompletion.isVisible = true
            percentAnimator.duration = trainingState.durationMs
            percentAnimator.start()
        }
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)
    }
}