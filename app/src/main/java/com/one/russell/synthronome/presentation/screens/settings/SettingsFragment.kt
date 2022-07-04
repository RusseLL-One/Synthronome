package com.one.russell.synthronome.presentation.screens.settings

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.one.russell.synthronome.R
import com.one.russell.synthronome.databinding.FragmentSettingsBinding
import com.one.russell.synthronome.domain.Colors
import com.one.russell.synthronome.domain.Constants
import com.one.russell.synthronome.presentation.alerts.ColorPickerDialogFragment
import com.one.russell.synthronome.presentation.views.utils.createHSLColor
import com.one.russell.synthronome.presentation.views.utils.addListener
import com.one.russell.synthronome.presentation.views.utils.setSwitchColor
import com.one.russell.synthronome.repeatOnResume
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val binding by viewBinding(FragmentSettingsBinding::bind)
    private val viewModel: SettingsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initOptionsValues()

        repeatOnResume {
            viewModel.colors.collect {
                setupColors(it)
            }
        }

        binding.llFlashContainer.isVisible = viewModel.isFlashAvailable

        binding.vSoundPresetPicker.view.wrapSelectorWheel = false
        binding.vSoundPresetPicker.view.minValue = Constants.MIN_SOUND_PRESET_ID
        binding.vSoundPresetPicker.view.maxValue = Constants.Max_SOUND_PRESET_ID
        binding.vSoundPresetPicker.view.setOnValueChangedListener { _, _, newVal ->
            viewModel.onSoundPresetChanged(newVal)
        }

        binding.vColorPrimaryIndicator.setOnClickListener {
            ColorPickerDialogFragment()
                .setInitColor(viewModel.colors.value.colorPrimary)
                .setOnColorPickedListener { viewModel.setColorPrimary(it) }
                .show(childFragmentManager, ColorPickerDialogFragment.TAG)
        }

        binding.vBackgroundBrightnessSlide.addListener {
            viewModel.setBackgroundLightness(it.floatL)
        }

        binding.sVibrationSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setVibrationEnabled(isChecked)
        }

        binding.sFlashSwitch.setOnCheckedChangeListener { _, isChecked ->
            viewModel.setFlashEnabled(isChecked)
        }
    }

    private fun initOptionsValues() {
        val currentOptions = viewModel.getCurrentOptions()
        binding.vSoundPresetPicker.view.value = currentOptions.soundPresetId
        binding.sFlashSwitch.isChecked = currentOptions.isFlashEnabled
        binding.sVibrationSwitch.isChecked = currentOptions.isVibrationEnabled
        // color changers receive their values from color provider
    }

    private fun setupColors(colors: Colors) {
        binding.vSoundPresetPicker.setupPaints(colors.colorPrimary, colors.colorSecondary)
        binding.sVibrationSwitch.setSwitchColor(colors.colorOnBackground, colors.colorPrimary)
        binding.sFlashSwitch.setSwitchColor(colors.colorOnBackground, colors.colorPrimary)
        binding.vColorPrimaryIndicator.setupPaints(colors.colorOnBackground, colors.colorPrimary)
        binding.vBackgroundBrightnessSlide.pickedColor = createHSLColor(colors.colorBackground)

        binding.tvSoundPreset.setTextColor(colors.colorOnBackground)
        binding.vSoundPresetPicker.setTextColor(colors.colorOnBackground)
        binding.tvVibration.setTextColor(colors.colorOnBackground)
        binding.tvFlash.setTextColor(colors.colorOnBackground)
        binding.tvColorPrimary.setTextColor(colors.colorOnBackground)
        binding.tvColorBackground.setTextColor(colors.colorOnBackground)
    }
}
