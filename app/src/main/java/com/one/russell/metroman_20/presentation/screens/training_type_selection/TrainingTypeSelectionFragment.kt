package com.one.russell.metroman_20.presentation.screens.training_type_selection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.one.russell.metroman_20.databinding.FragmentTrainingTypeSelectionBinding
import com.one.russell.metroman_20.presentation.screens.training_type_selection.adapter.TrainingTypesAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class TrainingTypeSelectionFragment : Fragment() {

    private var binding: FragmentTrainingTypeSelectionBinding? = null
    private val viewModel: TrainingTypeSelectionViewModel by viewModel()

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

        binding?.rvList?.adapter = TrainingTypesAdapter {
            Toast.makeText(requireContext(), it.type.name, Toast.LENGTH_SHORT).show()
        }.apply {
            submitList(viewModel.getTrainingTypeItems())
        }
    }

    private fun initBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentTrainingTypeSelectionBinding {
        return FragmentTrainingTypeSelectionBinding.inflate(inflater, container, false)
    }
}