package com.one.russell.metroman_20.presentation.screens.training.bar_dropping_subtype_selection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.one.russell.metroman_20.R
import com.one.russell.metroman_20.databinding.FragmentTrainingTypeSelectionBinding
import com.one.russell.metroman_20.domain.TrainingFinalType
import com.one.russell.metroman_20.presentation.base_components.BaseFragment
import com.one.russell.metroman_20.presentation.screens.training.bar_dropping_subtype_selection.adapter.BarDroppingSubtypesAdapter
import com.one.russell.metroman_20.presentation.screens.training.tempo_increasing_subtype_selection.TempoIncreasingSubtypeSelectionFragmentDirections
import org.koin.androidx.viewmodel.ext.android.viewModel

class BarDroppingSubtypeSelectionFragment : BaseFragment<FragmentTrainingTypeSelectionBinding>() {

    private val viewModel: BarDroppingSubtypeSelectionViewModel by viewModel()

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentTrainingTypeSelectionBinding {
        return FragmentTrainingTypeSelectionBinding.inflate(inflater, container, false).apply {
            rvList.adapter = BarDroppingSubtypesAdapter {
                navigateNext(it.type)
            }.apply {
                submitList(viewModel.getBarDroppingSubtypeItems())
            }
        }
    }

    private fun navigateNext(selectedType: BarDroppingSubtype) {
        val trainingFinalType = when (selectedType) {
            BarDroppingSubtype.RANDOMLY -> TrainingFinalType.BAR_DROPPING_RANDOMLY
            BarDroppingSubtype.BY_VALUE -> TrainingFinalType.BAR_DROPPING_BY_VALUE
        }

        BarDroppingSubtypeSelectionFragmentDirections
            .actionBarDroppingSubtypeSelectionFragmentToOptionsFragment(trainingFinalType)
            .let { findNavController().navigate(it) }
    }
}