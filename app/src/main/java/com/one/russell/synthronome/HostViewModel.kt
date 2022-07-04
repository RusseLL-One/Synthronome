package com.one.russell.synthronome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.one.russell.synthronome.domain.usecases.RestoreSavedValuesUseCase
import com.one.russell.synthronome.domain.usecases.SaveValuesUseCase
import com.one.russell.synthronome.domain.usecases.colors.ObserveColorsUseCase
import kotlinx.coroutines.launch

class HostViewModel(
    private val saveValuesUseCase: SaveValuesUseCase,
    private val restoreSavedValuesUseCase: RestoreSavedValuesUseCase,
    private val observeColorsUseCase: ObserveColorsUseCase
) : ViewModel() {

    val colors get() = observeColorsUseCase.execute()

    fun restoreValues() {
        viewModelScope.launch {
            restoreSavedValuesUseCase.execute()
        }
    }

    fun saveValues() {
        viewModelScope.launch {
            saveValuesUseCase.execute()
        }
    }
}