package com.one.russell.synthronome.presentation.alerts

import android.app.Dialog
import android.graphics.drawable.PaintDrawable
import android.os.Bundle
import android.view.View
import androidx.annotation.ColorInt
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.one.russell.synthronome.R
import com.one.russell.synthronome.databinding.DialogColorPickerBinding
import com.one.russell.synthronome.toPx

class ColorPickerDialogFragment : DialogFragment(R.layout.dialog_color_picker) {

    private val binding by viewBinding(DialogColorPickerBinding::bind)

    private var onColorPickedListener: ((color: Int) -> Unit)? = null

    @ColorInt private var colorPrimary: Int = 0
    @ColorInt private var colorSecondary: Int = 0
    @ColorInt private var colorBackground: Int = 0
    @ColorInt private var colorOnBackground: Int = 0

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.tvTitle.setTextColor(colorOnBackground)

        binding.colorPicker.addSVBar(binding.svbar)
        binding.colorPicker.oldCenterColor = colorPrimary
        binding.colorPicker.color = colorPrimary

        binding.btnApply.setupPaints(colorPrimary, colorSecondary, colorOnBackground)

        binding.btnApply.setOnClickListener {
            onColorPickedListener?.invoke(binding.colorPicker.color)
            dismiss()
        }

        super.onViewCreated(view, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            val dialogBg = PaintDrawable(colorBackground)
                .apply { setCornerRadius(CORNER_RADIUS_DP.toPx()) }

            window?.setBackgroundDrawable(dialogBg)
        }
    }

    fun setupColors(
        @ColorInt colorPrimary: Int,
        @ColorInt colorSecondary: Int,
        @ColorInt colorBackground: Int,
        @ColorInt colorOnBackground: Int,
    ): ColorPickerDialogFragment {
        this.colorPrimary = colorPrimary
        this.colorSecondary = colorSecondary
        this.colorBackground = colorBackground
        this.colorOnBackground = colorOnBackground
        return this
    }

    fun setOnColorPickedListener(onColorPicked: (color: Int) -> Unit): ColorPickerDialogFragment {
        onColorPickedListener = onColorPicked
        return this
    }

    companion object {
        const val TAG = "COLOR_PICKER_DIALOG_FRAGMENT"
        const val CORNER_RADIUS_DP = 16
    }
}