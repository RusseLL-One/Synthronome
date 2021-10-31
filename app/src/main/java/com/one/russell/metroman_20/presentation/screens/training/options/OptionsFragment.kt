package com.one.russell.metroman_20.presentation.screens.training.options

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.one.russell.metroman_20.R
import com.one.russell.metroman_20.databinding.FragmentTrainingOptionsBinding
import com.one.russell.metroman_20.presentation.base_components.BaseFragment
import com.one.russell.metroman_20.presentation.screens.training.options.adapter.AdjustersAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class OptionsFragment : BaseFragment<FragmentTrainingOptionsBinding>() {

    private val args: OptionsFragmentArgs by navArgs()
    private val viewModel: OptionsViewModel by viewModel()

    private val adapter = AdjustersAdapter()

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTrainingOptionsBinding {
        return FragmentTrainingOptionsBinding.inflate(inflater, container, false).apply {
            list.adapter = adapter.apply {
                submitList(viewModel.getAdjusters(args.trainingFinalType))
            }
            list.setOptionsGridLayoutManager()

            btnProceed.setOnClickListener {
                viewModel.submit(args.trainingFinalType)
                findNavController().navigate(R.id.action_optionsFragment_to_mainFragment)
            }
        }
    }

    private fun RecyclerView.setOptionsGridLayoutManager() {
        layoutManager = GridLayoutManager(requireContext(), GRID_SPAN_COUNT).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    val isLastItemAndShouldBeCentered = adapter?.itemCount?.let { itemCount ->
                        val isLastItem = position == itemCount - 1
                        val isShouldBeCentered = itemCount % GRID_SPAN_COUNT == 1
                        isLastItem && isShouldBeCentered
                    } ?: false

                    return if (isLastItemAndShouldBeCentered) GRID_SPAN_COUNT else 1
                }
            }
        }
    }

    companion object {
        private const val GRID_SPAN_COUNT = 2
    }
}