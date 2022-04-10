package com.one.russell.metroman_20.presentation.screens.main

import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.one.russell.metroman_20.R
import com.one.russell.metroman_20.databinding.FragmentMainBinding
import com.one.russell.metroman_20.domain.ClickState
import com.one.russell.metroman_20.domain.TrainingProcessor.Companion.TRAINING_TIME_INFINITE
import com.one.russell.metroman_20.domain.TrainingState
import com.one.russell.metroman_20.presentation.base_components.BaseFragment
import com.one.russell.metroman_20.repeatOnResume
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : BaseFragment<FragmentMainBinding>() {

    private val viewModel: MainViewModel by viewModel()

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentMainBinding {
        return FragmentMainBinding.inflate(inflater, container, false).apply {
            repeatOnResume {
                viewModel.bpm.collect { bpm ->
                    tvBpm.text = getString(R.string.main_bpm, bpm)
                    vKnob.setGlowIntense(bpm)
                }
            }

            repeatOnResume {
                viewModel.clickState.collect {
                    vStart.setButtonPressed(it == ClickState.STARTED)
                }
            }

            repeatOnResume {
                viewModel.trainingState.collect { state ->
                    when (state) {
                        TrainingState.Idle -> {
                            trainingInfo.isVisible = false
                            trainingCompletion.isVisible = false
                            vKnob.setIsBlocked(false)
                        }
                        is TrainingState.Running -> {
                            trainingInfo.isVisible = true
                            trainingCompletion.isVisible = true
                            vKnob.setIsBlocked(state.shouldBlockControls)

                            if (state.durationMs != TRAINING_TIME_INFINITE) {
                                ValueAnimator
                                    .ofFloat(0f, 1f)
                                    .setDuration(state.durationMs)
                                    .apply {
                                        interpolator = LinearInterpolator()
                                        addUpdateListener {
                                            val percent =
                                                (100 * (it.animatedValue as Float)).toInt()
                                            trainingCompletion.text = "$percent%"
                                        }
                                    }.start()
                            }
                        }
                    }
                }
            }

            repeatOnResume {
                viewModel.clickerCallback.collect {
                    beatline.animateBall(it)
                    beatline2.animateBall(it)
                }
            }

            vKnob.addOnValueChangedCallback {
                viewModel.onBpmChanged(delta = it)
            }

            vStart.setOnClickListener {
                viewModel.onPlayClicked()
            }

            tap.onTapClickedListener = { bpm ->
                viewModel.onTapClicked(bpm)
            }

            openTraining.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_typeSelectionFragment)
            }
        }
    }
}