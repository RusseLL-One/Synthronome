package com.one.russell.metroman_20.presentation.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.one.russell.metroman_20.domain.ClickState
import com.one.russell.metroman_20.domain.ClickState.IDLE
import com.one.russell.metroman_20.domain.ClickState.STARTED
import com.one.russell.metroman_20.domain.Constants.BPM_DEBOUNCE_TIME_MILLIS
import com.one.russell.metroman_20.domain.Constants.MAX_BPM
import com.one.russell.metroman_20.domain.Constants.MIN_BPM
import com.one.russell.metroman_20.domain.TrainingState
import com.one.russell.metroman_20.domain.usecases.*
import com.one.russell.metroman_20.domain.usecases.training.ObserveTrainingDataUseCase
import com.one.russell.metroman_20.domain.usecases.training.ObserveTrainingStateUseCase
import com.one.russell.metroman_20.domain.usecases.training.StartTrainingUseCase
import com.one.russell.metroman_20.domain.usecases.training.StopTrainingUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel(
    private val playRotateClickUseCase: PlayRotateClickUseCase,
    private val setBpmUseCase: SetBpmUseCase,
    private val getSavedBpmUseCase: GetSavedBpmUseCase,
    private val saveCurrentBpmUseCase: SaveCurrentBpmUseCase,
    private val startClickingUseCase: StartClickingUseCase,
    private val stopClickingUseCase: StopClickingUseCase,
    private val observeClickStateUseCase: ObserveClickStateUseCase,
    private val getClickerCallbackUseCase: GetClickerCallbackUseCase,
    private val observeTrainingDataUseCase: ObserveTrainingDataUseCase,
    private val startTrainingUseCase: StartTrainingUseCase,
    private val stopTrainingUseCase: StopTrainingUseCase,
    private val observeTrainingStateUseCase: ObserveTrainingStateUseCase
) : ViewModel() {

    private val _bpm = MutableStateFlow(0)
    val bpm: StateFlow<Int>
        get() = _bpm

    private val _trainingState: MutableStateFlow<TrainingState> = MutableStateFlow(TrainingState.Idle)
    val trainingState: StateFlow<TrainingState>
        get() = _trainingState

    private var _clickState = MutableStateFlow(IDLE)
    val clickState: StateFlow<ClickState>
        get() = _clickState

    private val _clickerCallback = MutableSharedFlow<Int>()
    val clickerCallback: SharedFlow<Int>
        get() = _clickerCallback

    init {
        getSavedBpm()
        saveDebouncedBpm()
        observeClickState()
        observeClickerCallback()
        observeTrainingData()
        observeTrainingState()
    }

    private fun getSavedBpm() {
        viewModelScope.launch {
            setBpm(getSavedBpmUseCase.execute())
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
            observeClickStateUseCase.execute().collect {
                _clickState.emit(it)
            }
        }
    }

    private fun observeClickerCallback() {
        viewModelScope.launch {
            getClickerCallbackUseCase.execute().onClick.collect {
                _clickerCallback.emit(it)
            }
        }
    }

    private fun observeTrainingData() {
        viewModelScope.launch {
            observeTrainingDataUseCase.execute().collect { trainingData ->
                startTrainingUseCase.execute(trainingData, _bpm)
            }
        }
    }

    private fun observeTrainingState() {
        viewModelScope.launch {
            observeTrainingStateUseCase.execute().collect {
                _trainingState.emit(it)
            }
        }
    }

    fun onBpmChanged(delta: Int) {
        val newBpm = (_bpm.value + delta).coerceIn(MIN_BPM, MAX_BPM)
        playRotateClickUseCase.execute()
        setBpm(newBpm)
    }

    fun onPlayClicked() {
        when (_clickState.value) {
            IDLE -> startClickingUseCase.execute()
            STARTED -> {
                stopClickingUseCase.execute()
                stopTrainingUseCase.execute()
            }
        }
    }

    fun onTapClicked(bpm: Int?) {
        playRotateClickUseCase.execute()
        if (bpm != null) {
            setBpm(bpm)
        }
    }

    private fun setBpm(bpm: Int) {
        setBpmUseCase.execute(bpm)
        viewModelScope.launch {
            _bpm.emit(bpm)
        }
    }
}