package com.one.russell.metroman_20.presentation.screens.training_type_selection

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.one.russell.metroman_20.databinding.FragmentTrainingTypeSelectionBinding
import com.one.russell.metroman_20.presentation.base_components.BaseFragment
import com.one.russell.metroman_20.presentation.screens.training_type_selection.adapter.TrainingTypesAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class TrainingTypeSelectionFragment : BaseFragment<FragmentTrainingTypeSelectionBinding>() {

    private val viewModel: TrainingTypeSelectionViewModel by viewModel()

    override fun initBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentTrainingTypeSelectionBinding {
        return FragmentTrainingTypeSelectionBinding.inflate(inflater, container, false).apply {
            rvList.adapter = TrainingTypesAdapter {
                Toast.makeText(requireContext(), it.type.name, Toast.LENGTH_SHORT).show() // todo
            }.apply {
                submitList(viewModel.getTrainingTypeItems())
            }
        }
    }
}