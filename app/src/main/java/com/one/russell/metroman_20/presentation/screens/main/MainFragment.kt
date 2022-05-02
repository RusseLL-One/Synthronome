package com.one.russell.metroman_20.presentation.screens.main

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.one.russell.metroman_20.R
import com.one.russell.metroman_20.databinding.FragmentMainBinding
import com.one.russell.metroman_20.domain.ClickState
import com.one.russell.metroman_20.domain.Constants
import com.one.russell.metroman_20.domain.TrainingState
import com.one.russell.metroman_20.repeatOnResume
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment(R.layout.fragment_main) {

    private val binding by viewBinding(FragmentMainBinding::bind)
    private val viewModel: MainViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
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
                            vTrainingOverlay.isVisible = false
                            vKnob.setIsBlocked(false)
                        }
                        is TrainingState.Running -> {
                            vTrainingOverlay.isVisible = true
                            vTrainingOverlay.showTrainingCompletion(state)
                            vKnob.setIsBlocked(state.shouldBlockControls)
                        }
                    }
                }
            }

            repeatOnResume {
                viewModel.clickerCallback.collect {
                    beatline.animateBall(it.bpm)
                }
            }

            repeatOnResume {
                viewModel.beatTypes.collect { beatTypes ->
                    if (vTimeSignature.view.value != beatTypes.size) {
                        vTimeSignature.view.value = beatTypes.size
                    }
                    vBeatTypesContainer.setBeatTypes(beatTypes)
                }
            }

            repeatOnResume {
                viewModel.colors.collect {
                    root.setBackgroundColor(it.backgroundColor)
                    vBeatTypesContainer.setColors(it.primaryColor, it.secondaryColor)
                    beatline.setupPaints(it.primaryColor, it.secondaryColor)
                    vKnob.setupPaints(it.primaryColor, it.secondaryColor)
                    vStart.setupPaints(it.primaryColor, it.secondaryColor)
                    btnTap.setupPaints(it.primaryColor, it.secondaryColor)
                    btnSettings.setupPaints(it.primaryColor, it.secondaryColor)
                    btnTraining.setupPaints(it.primaryColor, it.secondaryColor)
                    btnBookmarks.setupPaints(it.primaryColor, it.secondaryColor)
                    btnAddBookmark.setupPaints(it.primaryColor, it.secondaryColor)
                    vTrainingOverlay.setupPaints(it.primaryColor, it.secondaryColor)
                    vTimeSignature.setupPaints(it.primaryColor, it.secondaryColor)
                }
            }

            vKnob.addOnValueChangedCallback {
                viewModel.onBpmChanged(delta = it)
            }

            vStart.setOnClickListener {
                viewModel.onPlayClicked()
            }

            btnTap.setOnClickListener {
                viewModel.onTapClicked()
            }

            vTimeSignature.view.wrapSelectorWheel = false
            vTimeSignature.view.minValue = Constants.MIN_BEATS_IN_BAR_COUNT
            vTimeSignature.view.maxValue = Constants.MAX_BEATS_IN_BAR_COUNT
            vTimeSignature.view.setOnValueChangedListener { _, _, newVal ->
                viewModel.onTimeSignatureChanged(newVal)
            }

            vBeatTypesContainer.setOnBeatTypeClickListener { index ->
                viewModel.onBeatTypeClicked(index)
            }

            btnTraining.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_typeSelectionFragment)
            }

            btnSettings.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_settingsFragment)
            }
        }
    }
}