package com.one.russell.synthronome.presentation.screens.main

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.one.russell.synthronome.R
import com.one.russell.synthronome.databinding.FragmentMainBinding
import com.one.russell.synthronome.domain.ClickState
import com.one.russell.synthronome.domain.Constants
import com.one.russell.synthronome.domain.TrainingState
import com.one.russell.synthronome.repeatOnResume
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
                            vKnob.isLocked = false
                            btnBookmarks.isLocked = false
                            btnTap.isLocked = false
                        }
                        is TrainingState.Running -> {
                            vTrainingOverlay.isVisible = true
                            vTrainingOverlay.showTrainingCompletion(state)
                            vKnob.isLocked = state.shouldBlockControls
                            btnBookmarks.isLocked = state.shouldBlockControls
                            btnTap.isLocked = state.shouldBlockControls
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
                viewModel.isAnyBookmarkSelected.collect {
                    btnAddBookmark.setImage(
                        if (it) R.drawable.ic_bookmark_selected
                        else R.drawable.ic_bookmark_unselected
                    )
                }
            }

            repeatOnResume {
                viewModel.colors.collect {
                    root.setBackgroundColor(it.colorBackground)
                    vBeatTypesContainer.setColors(it.colorPrimary, it.colorSecondary)
                    beatline.setupPaints(it.colorPrimary, it.colorSecondary)
                    vKnob.setupPaints(it.colorPrimary, it.colorSecondary)
                    vStart.setupPaints(it.colorPrimary, it.colorSecondary, it.colorOnPrimary)
                    btnTap.setupColors(it.colorPrimary, it.colorSecondary, it.colorOnBackground)
                    btnSettings.setupColors(it.colorPrimary, it.colorSecondary, it.colorOnBackground)
                    btnTraining.setupColors(it.colorPrimary, it.colorSecondary, it.colorOnBackground)
                    btnBookmarks.setupColors(it.colorPrimary, it.colorSecondary, it.colorOnBackground)
                    btnAddBookmark.setupColors(it.colorPrimary, it.colorSecondary, it.colorOnBackground)
                    vTrainingOverlay.setupPaints(it.colorPrimary, it.colorSecondary, it.colorOnPrimary)
                    vTimeSignature.setupPaints(it.colorPrimary, it.colorSecondary)

                    tvBpm.setTextColor(it.colorOnBackground)
                    vTimeSignature.setTextColor(it.colorOnBackground)
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

            btnAddBookmark.setOnClickListener {
                viewModel.onAddBookmarkClicked()
            }

            btnBookmarks.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_bookmarksFragment)
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