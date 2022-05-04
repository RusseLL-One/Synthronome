package com.one.russell.metroman_20.presentation.screens.training.training_subtype_selection

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.one.russell.metroman_20.R
import com.one.russell.metroman_20.databinding.FragmentTrainingTypeSelectionBinding
import com.one.russell.metroman_20.domain.TrainingFinalType
import com.one.russell.metroman_20.presentation.screens.training.training_subtype_selection.adapter.TrainingSubtypesAdapter
import com.one.russell.metroman_20.presentation.views.utils.createPaddingsDecoration
import com.one.russell.metroman_20.presentation.views.utils.disableScrolling
import com.one.russell.metroman_20.toPx
import org.koin.androidx.viewmodel.ext.android.viewModel

class TrainingSubtypeSelectionFragment : Fragment(R.layout.fragment_training_type_selection) {

    private val args: TrainingSubtypeSelectionFragmentArgs by navArgs()
    private val binding by viewBinding(FragmentTrainingTypeSelectionBinding::bind)
    private val viewModel: TrainingSubtypeSelectionViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.setBackgroundColor(viewModel.colors.colorBackground)

        binding.rvList.disableScrolling()
        binding.rvList.addItemDecoration(createPaddingsDecoration(verticalPadding = 16.toPx()))
        binding.rvList.adapter = TrainingSubtypesAdapter {
            navigateNext(it.type)
        }.apply {
            items = viewModel.getTrainingSubtypeItems(args.trainingTopLevelType)
        }
    }

    private fun navigateNext(trainingFinalType: TrainingFinalType) {
        TrainingSubtypeSelectionFragmentDirections
            .actionSubtypeSelectionFragmentToOptionsFragment(trainingFinalType)
            .let { findNavController().navigate(it) }
    }
}