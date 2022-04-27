package com.one.russell.metroman_20.presentation.screens.training.options

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.one.russell.metroman_20.R
import com.one.russell.metroman_20.databinding.FragmentTrainingOptionsBinding
import com.one.russell.metroman_20.presentation.base_components.BaseFragment
import com.one.russell.metroman_20.presentation.screens.training.options.adapter.AdjustersAdapter
import com.one.russell.metroman_20.presentation.views.utils.createPaddingsDecoration
import com.one.russell.metroman_20.toPx
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class OptionsFragment : BaseFragment<FragmentTrainingOptionsBinding>() {

    private val args: OptionsFragmentArgs by navArgs()
    private val viewModel: OptionsViewModel by viewModel()

    private val adapter by lazy { AdjustersAdapter(viewModel::setListItemValue) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.createAdjustersList(args.trainingFinalType)
    }

    override fun initBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTrainingOptionsBinding {
        return FragmentTrainingOptionsBinding.inflate(inflater, container, false).apply {
            rvList.adapter = adapter
            rvList.setOptionsGridLayoutManager()
            rvList.addItemDecoration(
                createPaddingsDecoration(horizontalPadding = 64.toPx(), verticalPadding = 32.toPx())
            )

            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.adjustersListItems.collect {
                    adapter.items = it
                }
            }

            btnProceed.setupPaints(viewModel.colors.primaryColor, viewModel.colors.secondaryColor)
            btnProceed.setOnClickListener {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.submit(args.trainingFinalType)
                    findNavController().popBackStack(R.id.mainFragment, false)
                }
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