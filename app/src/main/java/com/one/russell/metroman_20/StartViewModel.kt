package com.one.russell.metroman_20

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.one.russell.metroman_20.domain.usecases.RestoreSavedValuesUseCase
import kotlinx.coroutines.launch

class StartViewModel(
    private val restoreSavedValuesUseCase: RestoreSavedValuesUseCase
) : ViewModel() {

    init {
        viewModelScope.launch {
            restoreSavedValuesUseCase.execute()
        }
    }
}