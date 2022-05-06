package com.one.russell.synthronome.presentation.screens.bookmarks

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.one.russell.synthronome.R
import com.one.russell.synthronome.databinding.FragmentBookmarksBinding
import com.one.russell.synthronome.presentation.screens.bookmarks.adapter.BookmarksAdapter
import com.one.russell.synthronome.presentation.screens.bookmarks.adapter.toListItem
import com.one.russell.synthronome.presentation.views.utils.createPaddingsDecoration
import com.one.russell.synthronome.repeatOnResume
import com.one.russell.synthronome.toPx
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookmarksFragment : Fragment(R.layout.fragment_bookmarks) {

    private val binding by viewBinding(FragmentBookmarksBinding::bind)
    private val viewModel: BookmarksViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repeatOnResume {
            viewModel.bookmarks.collect { bookmarks ->
                binding.tvEmptyList.isVisible = bookmarks.isEmpty()
                binding.btnClear.isVisible = bookmarks.isNotEmpty()
                (binding.rvBookmarks.adapter as BookmarksAdapter).items = bookmarks.map {
                    it.toListItem(
                        viewModel.colors.colorPrimary,
                        viewModel.colors.colorSecondary,
                        viewModel.colors.colorOnBackground
                    )
                }
            }
        }

        binding.root.setBackgroundColor(viewModel.colors.colorBackground)
        binding.tvEmptyList.setTextColor(viewModel.colors.colorOnBackground)

        binding.rvBookmarks.adapter = BookmarksAdapter(
            onBookmarkClicked = viewModel::onBookmarkClicked,
            onRemoveClicked = viewModel::onBookmarkRemoved
        )
        binding.rvBookmarks.addItemDecoration(
            createPaddingsDecoration(
                horizontalPadding = 32.toPx(),
                verticalPadding = 16.toPx()
            )
        )

        binding.btnClear.setOnClickListener {
            viewModel.onClearClicked()
        }
    }
}
