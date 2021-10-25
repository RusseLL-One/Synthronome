package com.one.russell.metroman_20.presentation.screens.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.one.russell.metroman_20.R
import com.one.russell.metroman_20.databinding.FragmentMainBinding
import com.one.russell.metroman_20.domain.Constants.MAX_BPM
import com.one.russell.metroman_20.launchOnResume
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainFragment : Fragment() {

    private var binding: FragmentMainBinding? = null
    private val viewModel: MainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = initBinding(inflater, container)
        return binding!!.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        launchOnResume {
            viewModel.bpm.collect {
                binding?.tvBpm?.text = getString(R.string.main_bpm, it)
                binding?.vKnob?.setGlowIntense(it.toFloat() / MAX_BPM)
            }
        }

        binding?.vKnob?.addOnValueChangedCallback {
            viewModel.onBpmChanged(delta = it)
        }

        binding?.btnPlay?.setOnClickListener {
            viewModel.onPlayClicked()
        }
    }

    private fun initBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentMainBinding {
        return FragmentMainBinding.inflate(inflater, container, false)
    }
}