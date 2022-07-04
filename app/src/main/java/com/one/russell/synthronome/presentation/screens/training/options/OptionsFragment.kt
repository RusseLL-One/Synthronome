package com.one.russell.synthronome.presentation.screens.training.options

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.one.russell.synthronome.R
import com.one.russell.synthronome.databinding.FragmentTrainingOptionsBinding
import com.one.russell.synthronome.presentation.screens.training.options.adapter.AdjustersAdapter
import com.one.russell.synthronome.presentation.views.utils.createPaddingsDecoration
import com.one.russell.synthronome.toPx
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class OptionsFragment : Fragment(R.layout.fragment_training_options) {

    private val args: OptionsFragmentArgs by navArgs()
    private val binding by viewBinding(FragmentTrainingOptionsBinding::bind)
    private val viewModel: OptionsViewModel by viewModel()

    private val adapter by lazy { AdjustersAdapter(viewModel::setListItemValue) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.createAdjustersList(args.trainingFinalType)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupColors()

        binding.rvList.adapter = adapter
        binding.rvList.setOptionsGridLayoutManager()
        binding.rvList.addItemDecoration(
            createPaddingsDecoration(horizontalPadding = 64.toPx(), verticalPadding = 32.toPx())
        )

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.adjustersListItems.collect {
                adapter.items = it
            }
        }

        binding.btnProceed.setOnClickListener {
            viewModel.submit(args.trainingFinalType)
            findNavController().popBackStack(R.id.mainFragment, false)
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

    private fun setupColors() {
        binding.root.setBackgroundColor(viewModel.colors.colorBackground)
        binding.btnProceed.setupColors(
            viewModel.colors.colorPrimary,
            viewModel.colors.colorSecondary,
            viewModel.colors.colorOnBackground,
        )
    }

    companion object {
        private const val GRID_SPAN_COUNT = 2
    }
}