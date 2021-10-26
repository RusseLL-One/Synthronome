package com.one.russell.metroman_20.presentation.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.one.russell.metroman_20.domain.ClickState
import com.one.russell.metroman_20.domain.ClickState.IDLE
import com.one.russell.metroman_20.domain.ClickState.STARTED
import com.one.russell.metroman_20.domain.Constants.BPM_DEBOUNCE_TIME_MILLIS
import com.one.russell.metroman_20.domain.Constants.MAX_BPM
import com.one.russell.metroman_20.domain.Constants.MIN_BPM
import com.one.russell.metroman_20.domain.usecases.*
import com.one.russell.metroman_20.domain.wrappers.Clicker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch

class MainViewModel(
    getClickerUseCase: GetClickerUseCase,
    private val getSavedBpmUseCase: GetSavedBpmUseCase,
    private val saveCurrentBpmUseCase: SaveCurrentBpmUseCase,
    private val startClickingUseCase: StartClickingUseCase,
    private val stopClickingUseCase: StopClickingUseCase,
    private val observeClickStateUseCase: ObserveClickStateUseCase
) : ViewModel() {

    private val clicker: Clicker = getClickerUseCase.execute()

    private val _bpm = MutableStateFlow(0)
    val bpm: StateFlow<Int>
        get() = _bpm

    private var clickState: ClickState = IDLE

    init {
        getSavedBpm()
        saveDebouncedBpm()
        observeClickState()
    }

    private fun getSavedBpm() {
        viewModelScope.launch {
            val savedBpm = getSavedBpmUseCase.execute()
            _bpm.emit(savedBpm)
            clicker.setBpm(savedBpm)
        }
    }

    private fun saveDebouncedBpm() {
        viewModelScope.launch {
            _bpm
                .debounce(timeoutMillis = BPM_DEBOUNCE_TIME_MILLIS)
                .collect { saveCurrentBpmUseCase.execute(it) }
        }
    }

    private fun observeClickState() {
        viewModelScope.launch {
            observeClickStateUseCase.execute()
                .collect {
                    clickState = it
                }
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
        when (clickState) {
            IDLE -> startClickingUseCase.execute()
            STARTED -> stopClickingUseCase.execute()
        }
    }

    fun onTapClicked(bpm: Int?) {
        clicker.playRotateClick()
        if (bpm != null) {
            clicker.setBpm(bpm)

            viewModelScope.launch {
                _bpm.emit(bpm)
            }
        }
    }
}