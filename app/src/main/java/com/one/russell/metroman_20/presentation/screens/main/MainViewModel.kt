package com.one.russell.metroman_20.presentation.screens.main

import androidx.annotation.ColorInt
import androidx.lifecycle.ViewModel
import com.one.russell.metroman_20.domain.BeatType
import com.one.russell.metroman_20.domain.ClickState
import com.one.russell.metroman_20.domain.ClickState.IDLE
import com.one.russell.metroman_20.domain.ClickState.STARTED
import com.one.russell.metroman_20.domain.Colors
import com.one.russell.metroman_20.domain.Constants.MAX_BPM
import com.one.russell.metroman_20.domain.Constants.MIN_BPM
import com.one.russell.metroman_20.domain.TrainingState
import com.one.russell.metroman_20.domain.usecases.*
import com.one.russell.metroman_20.domain.usecases.colors.ObserveColorsUseCase
import com.one.russell.metroman_20.domain.usecases.colors.SetColorsUseCase
import com.one.russell.metroman_20.domain.usecases.training.ObserveTrainingStateUseCase
import com.one.russell.metroman_20.domain.usecases.training.StopTrainingUseCase
import kotlinx.coroutines.flow.*

class MainViewModel(
    private val playRotateClickUseCase: PlayRotateClickUseCase,
    private val setBpmUseCase: SetBpmUseCase,
    private val observeBpmUseCase: ObserveBpmUseCase,
    private val setTimeSignatureUseCase: SetTimeSignatureUseCase,
    private val incrementBeatTypeUseCase: IncrementBeatTypeUseCase,
    private val observeBeatTypesUseCase: ObserveBeatTypesUseCase,
    private val startClickingUseCase: StartClickingUseCase,
    private val stopClickingUseCase: StopClickingUseCase,
    private val observeClickStateUseCase: ObserveClickStateUseCase,
    private val getClickerCallbackUseCase: GetClickerCallbackUseCase,
    private val stopTrainingUseCase: StopTrainingUseCase,
    private val observeTrainingStateUseCase: ObserveTrainingStateUseCase,
    private val observeColorsUseCase: ObserveColorsUseCase,
    private val setColorsUseCase: SetColorsUseCase,
    private val calcBpmByTapIntervalUseCase: CalcBpmByTapIntervalUseCase
) : ViewModel() {

    val bpm: StateFlow<Int>
        get() = observeBpmUseCase.execute()

    val beatTypes: StateFlow<List<BeatType>>
        get() = observeBeatTypesUseCase.execute()

    val colors: StateFlow<Colors>
        get() = observeColorsUseCase.execute()

    val trainingState: StateFlow<TrainingState>
        get() = observeTrainingStateUseCase.execute()

    val clickState: StateFlow<ClickState>
        get() = observeClickStateUseCase.execute()

    val clickerCallback: SharedFlow<Int>
        get() = getClickerCallbackUseCase.execute().onClick

    fun onBpmChanged(delta: Int) {
        val newBpm = (bpm.value + delta).coerceIn(MIN_BPM, MAX_BPM)
        playRotateClickUseCase.execute()
        setBpmUseCase.execute(newBpm)
    }

    fun onPlayClicked() {
        when (clickState.value) {
            IDLE -> startClickingUseCase.execute()
            STARTED -> {
                stopClickingUseCase.execute()
                stopTrainingUseCase.execute()
            }
        }
    }

    fun onTapClicked() {
        playRotateClickUseCase.execute()
        calcBpmByTapIntervalUseCase.onTapClicked()
    }

    fun onTimeSignatureChanged(timeSignature: Int) {
        setTimeSignatureUseCase.execute(timeSignature)
    }

    fun onBeatTypeClicked(index: Int) {
        incrementBeatTypeUseCase.execute(index)
    }

    fun setPrimaryColor(@ColorInt color: Int) {
        setColorsUseCase.primary(color)
    }
}