package com.one.russell.metroman_20.presentation.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.one.russell.metroman_20.domain.Constants.BPM_DEBOUNCE_TIME_MILLIS
import com.one.russell.metroman_20.domain.Constants.MAX_BPM
import com.one.russell.metroman_20.domain.Constants.MIN_BPM
import com.one.russell.metroman_20.domain.usecases.GetClickerUseCase
import com.one.russell.metroman_20.domain.usecases.GetSavedBpmUseCase
import com.one.russell.metroman_20.domain.usecases.SaveCurrentBpmUseCase
import com.one.russell.metroman_20.domain.wrappers.Clicker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

class MainViewModel(
    getClickerUseCase: GetClickerUseCase,
    private val getSavedBpmUseCase: GetSavedBpmUseCase,
    private val saveCurrentBpmUseCase: SaveCurrentBpmUseCase
) : ViewModel() {

    private val clicker: Clicker = getClickerUseCase.execute()

    private val _bpm = MutableStateFlow(0)
    val bpm: StateFlow<Int>
        get() = _bpm

    private var isStarted = false // todo

    init {
        getSavedBpm()
        saveDebouncedBpm()
    }

    private fun getSavedBpm() {
        viewModelScope.launch {
            _bpm.value = getSavedBpmUseCase.execute()
        }
    }

    private fun saveDebouncedBpm() {
        viewModelScope.launch {
            _bpm
                .debounce(timeoutMillis = BPM_DEBOUNCE_TIME_MILLIS)
                .collect { saveCurrentBpmUseCase.execute(it) }
        }
    }

    fun onBpmChanged(delta: Int) {
        val newBpm = (_bpm.value + delta).coerceIn(MIN_BPM, MAX_BPM)
        clicker.playRotateClick()
        clicker.setBpm(newBpm)

        viewModelScope.launch {
            _bpm.emit(newBpm)
        }
    }

    fun onPlayClicked() {
        if (!isStarted) {
            clicker.start()
        } else {
            clicker.stop()
        }
        isStarted = !isStarted
    }
}