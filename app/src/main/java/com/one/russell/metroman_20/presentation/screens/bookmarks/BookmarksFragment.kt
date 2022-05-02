package com.one.russell.metroman_20.presentation.screens.bookmarks

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.one.russell.metroman_20.R
import com.one.russell.metroman_20.databinding.FragmentBookmarksBinding
import com.one.russell.metroman_20.databinding.FragmentSettingsBinding
import com.one.russell.metroman_20.domain.Colors
import com.one.russell.metroman_20.repeatOnResume
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class BookmarksFragment : Fragment(R.layout.fragment_bookmarks) {

    private val binding by viewBinding(FragmentBookmarksBinding::bind)
    private val viewModel: BookmarksViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        repeatOnResume {
            viewModel.colors.collect {
                setupColors(it)
            }
        }

    }



    private fun setupColors(colors: Colors) {
        binding.root.setBackgroundColor(colors.backgroundColor)

    }
}
