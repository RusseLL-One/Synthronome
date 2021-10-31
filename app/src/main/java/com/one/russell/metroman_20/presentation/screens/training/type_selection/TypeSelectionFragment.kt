package com.one.russell.metroman_20.presentation.screens.training.type_selection

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.one.russell.metroman_20.R
import com.one.russell.metroman_20.databinding.FragmentTrainingTypeSelectionBinding
import com.one.russell.metroman_20.domain.TrainingFinalType
import com.one.russell.metroman_20.presentation.base_components.BaseFragment
import com.one.russell.metroman_20.presentation.screens.training.type_selection.adapter.TypesAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class TypeSelectionFragment : BaseFragment<FragmentTrainingTypeSelectionBinding>() {

    private val viewModel: TypeSelectionViewModel by viewModel()

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentTrainingTypeSelectionBinding {
        return FragmentTrainingTypeSelectionBinding.inflate(inflater, container, false).apply {
            rvList.adapter = TypesAdapter {
                navigateNext(it.type)
            }.apply {
                submitList(viewModel.getTrainingTypeItems())
            }
        }
    }

    private fun navigateNext(selectedType: Type) {
        when (selectedType) {
            Type.TEMPO_INCREASING -> findNavController().navigate(R.id.action_typeSelectionFragment_to_tempoIncreasingSubtypeSelectionFragment)
            Type.BAR_DROPPING -> findNavController().navigate(R.id.action_typeSelectionFragment_to_barDroppingSubtypeSelectionFragment)
            Type.BEAT_DROPPING -> TypeSelectionFragmentDirections.actionTypeSelectionFragmentToOptionsFragment(TrainingFinalType.BEAT_DROPPING)
                .let { findNavController().navigate(it) }
        }
    }
}