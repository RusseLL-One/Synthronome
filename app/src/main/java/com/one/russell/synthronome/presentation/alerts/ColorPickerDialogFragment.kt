package com.one.russell.synthronome.presentation.alerts

import android.os.Bundle
import android.view.View
import androidx.annotation.ColorInt
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.one.russell.synthronome.R
import com.one.russell.synthronome.databinding.DialogColorPickerBinding

class ColorPickerDialogFragment : DialogFragment(R.layout.dialog_color_picker) {

    // todo доработать

    private val binding by viewBinding(DialogColorPickerBinding::bind)

    private var onColorPickedListener: ((color: Int) -> Unit)? = null
    @ColorInt private var initColor: Int? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.colorPicker.addSVBar(binding.svbar)
        initColor?.let {
            binding.colorPicker.oldCenterColor = it
            binding.colorPicker.color = it
        }

        binding.btnOk.setOnClickListener {
            onColorPickedListener?.invoke(binding.colorPicker.color)
            dismiss()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    fun setInitColor(@ColorInt color: Int): ColorPickerDialogFragment {
        initColor = color
        return this
    }

    fun setOnColorPickedListener(onColorPicked: (color: Int) -> Unit): ColorPickerDialogFragment {
        onColorPickedListener = onColorPicked
        return this
    }

    companion object {
        const val TAG = "COLOR_PICKER_DIALOG_FRAGMENT"
    }
}