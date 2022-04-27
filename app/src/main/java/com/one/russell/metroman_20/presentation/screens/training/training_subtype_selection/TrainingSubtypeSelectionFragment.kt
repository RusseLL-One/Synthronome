package com.one.russell.metroman_20.presentation.screens.training.training_subtype_selection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.one.russell.metroman_20.databinding.FragmentTrainingTypeSelectionBinding
import com.one.russell.metroman_20.domain.TrainingFinalType
import com.one.russell.metroman_20.presentation.base_components.BaseFragment
import com.one.russell.metroman_20.presentation.screens.training.training_subtype_selection.adapter.TrainingSubtypesAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class TrainingSubtypeSelectionFragment : BaseFragment<FragmentTrainingTypeSelectionBinding>() {

    private val args: TrainingSubtypeSelectionFragmentArgs by navArgs()
    private val viewModel: TrainingSubtypeSelectionViewModel by viewModel()

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentTrainingTypeSelectionBinding {
        return FragmentTrainingTypeSelectionBinding.inflate(inflater, container, false).apply {
            rvList.adapter = TrainingSubtypesAdapter {
                navigateNext(it.type)
            }.apply {
                items = viewModel.getTrainingSubtypeItems(args.trainingTopLevelType)
            }
        }
    }

    private fun navigateNext(trainingFinalType: TrainingFinalType) {
        TrainingSubtypeSelectionFragmentDirections
            .actionBarDroppingSubtypeSelectionFragmentToOptionsFragment(trainingFinalType)
            .let { findNavController().navigate(it) }
    }
}