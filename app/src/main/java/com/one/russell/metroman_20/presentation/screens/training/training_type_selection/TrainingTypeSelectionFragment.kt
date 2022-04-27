package com.one.russell.metroman_20.presentation.screens.training.training_type_selection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.one.russell.metroman_20.databinding.FragmentTrainingTypeSelectionBinding
import com.one.russell.metroman_20.domain.TrainingFinalType
import com.one.russell.metroman_20.domain.TrainingTopLevelType
import com.one.russell.metroman_20.presentation.base_components.BaseFragment
import com.one.russell.metroman_20.presentation.screens.training.training_type_selection.adapter.TrainingTypesAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class TrainingTypeSelectionFragment : BaseFragment<FragmentTrainingTypeSelectionBinding>() {

    private val viewModel: TrainingTypeSelectionViewModel by viewModel()

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTrainingTypeSelectionBinding {
        return FragmentTrainingTypeSelectionBinding.inflate(inflater, container, false).apply {
            rvList.adapter = TrainingTypesAdapter {
                navigateNext(it.type)
            }.apply {
                items = viewModel.getTrainingTypeItems()
            }
        }
    }

    private fun navigateNext(selectedType: TrainingTopLevelType) {
        when (selectedType) {
            TrainingTopLevelType.TEMPO_INCREASING -> TrainingTypeSelectionFragmentDirections.actionTypeSelectionFragmentToBarDroppingSubtypeSelectionFragment(
                TrainingTopLevelType.TEMPO_INCREASING
            )
            TrainingTopLevelType.BAR_DROPPING -> TrainingTypeSelectionFragmentDirections.actionTypeSelectionFragmentToBarDroppingSubtypeSelectionFragment(
                TrainingTopLevelType.BAR_DROPPING
            )
            TrainingTopLevelType.BEAT_DROPPING -> TrainingTypeSelectionFragmentDirections.actionTypeSelectionFragmentToOptionsFragment(
                TrainingFinalType.BEAT_DROPPING
            )
        }.let { findNavController().navigate(it) }
    }
}