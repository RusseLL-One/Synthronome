package com.one.russell.metroman_20.presentation.screens.training.tempo_increasing_subtype_selection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.one.russell.metroman_20.R
import com.one.russell.metroman_20.databinding.FragmentTrainingTypeSelectionBinding
import com.one.russell.metroman_20.domain.TrainingFinalType
import com.one.russell.metroman_20.presentation.base_components.BaseFragment
import com.one.russell.metroman_20.presentation.screens.training.bar_dropping_subtype_selection.BarDroppingSubtype
import com.one.russell.metroman_20.presentation.screens.training.tempo_increasing_subtype_selection.adapter.TempoIncreasingSubtypesAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class TempoIncreasingSubtypeSelectionFragment : BaseFragment<FragmentTrainingTypeSelectionBinding>() {

    private val viewModel: TempoIncreasingSubtypeSelectionViewModel by viewModel()

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentTrainingTypeSelectionBinding {
        return FragmentTrainingTypeSelectionBinding.inflate(inflater, container, false).apply {
            rvList.adapter = TempoIncreasingSubtypesAdapter {
                navigateNext(it.type)
            }.apply {
                submitList(viewModel.getTempoIncreasingSubtypeItems())
            }
        }
    }

    private fun navigateNext(selectedType: TempoIncreasingSubtype) {
        val trainingFinalType = when (selectedType) {
            TempoIncreasingSubtype.BY_BARS -> TrainingFinalType.TEMPO_INCREASING_BY_BARS
            TempoIncreasingSubtype.BY_TIME -> TrainingFinalType.TEMPO_INCREASING_BY_TIME
        }

        TempoIncreasingSubtypeSelectionFragmentDirections
            .actionTempoIncreasingSubtypeSelectionFragmentToOptionsFragment(trainingFinalType)
            .let { findNavController().navigate(it) }
    }
}