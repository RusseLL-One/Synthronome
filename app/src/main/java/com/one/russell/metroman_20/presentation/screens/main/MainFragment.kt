package com.one.russell.metroman_20.presentation.screens.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.one.russell.metroman_20.R
import com.one.russell.metroman_20.databinding.FragmentMainBinding
import com.one.russell.metroman_20.domain.Constants.MAX_BPM
import com.one.russell.metroman_20.presentation.base_components.BaseFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : BaseFragment<FragmentMainBinding>() {

    private val viewModel: MainViewModel by viewModel()

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentMainBinding {
        return FragmentMainBinding.inflate(inflater, container, false).apply {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.bpm.collect {
                    tvBpm.text = getString(R.string.main_bpm, it)
                    vKnob.setGlowIntense(it.toFloat() / MAX_BPM)
                }
            }

            vKnob.addOnValueChangedCallback {
                viewModel.onBpmChanged(delta = it)
            }

            btnPlay.setOnClickListener {
                viewModel.onPlayClicked()
            }

            tap.onTapClickedListener = { bpm ->
                viewModel.onTapClicked(bpm)
            }

            openTraining.setOnClickListener {
                findNavController().navigate(R.id.action_mainFragment_to_trainingTypeSelectionFragment)
            }
        }
    }
}