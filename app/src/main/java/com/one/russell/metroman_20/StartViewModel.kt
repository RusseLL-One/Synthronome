package com.one.russell.metroman_20

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.one.russell.metroman_20.domain.usecases.RestoreSavedValuesUseCase
import com.one.russell.metroman_20.domain.usecases.SaveValuesUseCase
import kotlinx.coroutines.launch

class StartViewModel(
    private val saveValuesUseCase: SaveValuesUseCase,
    private val restoreSavedValuesUseCase: RestoreSavedValuesUseCase
) : ViewModel() {

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