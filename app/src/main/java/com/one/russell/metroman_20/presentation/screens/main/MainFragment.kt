package com.one.russell.metroman_20.presentation.screens.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.one.russell.metroman_20.R
import com.one.russell.metroman_20.databinding.FragmentMainBinding
import com.one.russell.metroman_20.domain.ClickState
import com.one.russell.metroman_20.domain.Constants
import com.one.russell.metroman_20.domain.TrainingState
import com.one.russell.metroman_20.presentation.alerts.showColorPickerDialog
import com.one.russell.metroman_20.presentation.base_components.BaseFragment
import com.one.russell.metroman_20.repeatOnResume
import kotlinx.coroutines.flow.collect
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
                    beatline.animateBall(it)
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
                showColorPickerDialog(requireContext()) {
                    viewModel.setPrimaryColor(it)
                }
            }
        }
    }
}