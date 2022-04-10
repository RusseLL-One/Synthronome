package com.one.russell.metroman_20.presentation.screens.training.options

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.one.russell.metroman_20.R
import com.one.russell.metroman_20.domain.TrainingData
import com.one.russell.metroman_20.domain.TrainingFinalType
import com.one.russell.metroman_20.domain.usecases.training.GetTrainingDataUseCase
import com.one.russell.metroman_20.domain.usecases.training.SetTrainingDataUseCase
import com.one.russell.metroman_20.domain.wrappers.Clicker
import com.one.russell.metroman_20.presentation.screens.training.options.OptionsAdjusterType.*
import com.one.russell.metroman_20.presentation.screens.training.options.adapter.KnobItem
import com.one.russell.metroman_20.presentation.screens.training.options.adapter.ListItem
import com.one.russell.metroman_20.presentation.screens.training.options.adapter.PickerItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OptionsViewModel(
    private val clicker: Clicker,
    private val setTrainingDataUseCase: SetTrainingDataUseCase,
    private val getTrainingDataUseCase: GetTrainingDataUseCase
) : ViewModel() {

    private val _adjustersValues = mapOf(
        TEMPO_INCREASING_START_BPM to MutableStateFlow(0),
        TEMPO_INCREASING_END_BPM to MutableStateFlow(0),
        TEMPO_INCREASING_BY_BARS_EVERY_BARS to MutableStateFlow(0),
        TEMPO_INCREASING_BY_BARS_INCREASE_ON to MutableStateFlow(0),
        TEMPO_INCREASING_BY_TIME_MINUTES to MutableStateFlow(0),
        BAR_DROPPING_RANDOMLY_CHANCE to MutableStateFlow(0),
        BAR_DROPPING_BY_VALUE_ORDINARY_BARS_COUNT to MutableStateFlow(0),
        BAR_DROPPING_BY_VALUE_MUTED_BARS_COUNT to MutableStateFlow(0),
        BEAT_DROPPING_CHANCE to MutableStateFlow(0),
    )

    private var _adjustersList = MutableStateFlow<List<ListItem>>(emptyList())
    val adjustersList: StateFlow<List<ListItem>>
        get() = _adjustersList

    fun createAdjustersList(trainingType: TrainingFinalType) {
        viewModelScope.launch {
            getSavedValues()
            _adjustersList.emit(getAdjusters(trainingType))
        }
    }

    private suspend fun getSavedValues() {
        getTrainingDataUseCase.execute().forEach {
            _adjustersValues[it.key]!!.value = it.value
        }
    }

    private fun getAdjusters(trainingFinalType: TrainingFinalType): List<ListItem> {
        return when (trainingFinalType) {
            TrainingFinalType.TEMPO_INCREASING_BY_BARS -> listOf(
                createKnobItem(TEMPO_INCREASING_START_BPM, R.string.training_tempoIncreasing_startBpm),
                createKnobItem(TEMPO_INCREASING_END_BPM, R.string.training_tempoIncreasing_endBpm),
                createPickerItem(TEMPO_INCREASING_BY_BARS_EVERY_BARS, R.string.training_tempoIncreasing_everyBars),
                createKnobItem(TEMPO_INCREASING_BY_BARS_INCREASE_ON, R.string.training_tempoIncreasing_increaseOn)
            )
            TrainingFinalType.TEMPO_INCREASING_BY_TIME -> listOf(
                createKnobItem(TEMPO_INCREASING_START_BPM, R.string.training_tempoIncreasing_startBpm),
                createKnobItem(TEMPO_INCREASING_END_BPM, R.string.training_tempoIncreasing_endBpm),
                createPickerItem(TEMPO_INCREASING_BY_TIME_MINUTES, R.string.training_tempoIncreasing_minutes)
            )
            TrainingFinalType.BAR_DROPPING_RANDOMLY -> listOf(
                createPickerItem(BAR_DROPPING_RANDOMLY_CHANCE, R.string.training_barDropping_chance)
            )
            TrainingFinalType.BAR_DROPPING_BY_VALUE -> listOf(
                createPickerItem(BAR_DROPPING_BY_VALUE_ORDINARY_BARS_COUNT, R.string.training_barDropping_ordinaryBarsCount),
                createPickerItem(BAR_DROPPING_BY_VALUE_MUTED_BARS_COUNT, R.string.training_barDropping_mutedBarsCount)
            )
            TrainingFinalType.BEAT_DROPPING -> listOf(
                createPickerItem(BEAT_DROPPING_CHANCE, R.string.training_beatDropping_chance)
            )
        }
    }

    private fun createKnobItem(type: OptionsAdjusterType, @StringRes titleRes: Int): KnobItem {
        return KnobItem(type, titleRes, _adjustersValues[type]!!) {
            val newValue = (_adjustersValues[type]!!.value + it).coerceIn(type.minValue, type.maxValue)
            _adjustersValues[type]!!.value = newValue
            clicker.playRotateClick()
        }
    }

    private fun createPickerItem(type: OptionsAdjusterType, @StringRes titleRes: Int): PickerItem {
        return PickerItem(type, titleRes, _adjustersValues[type]!!.value) {
            _adjustersValues[type]!!.value = it
        }
    }

    suspend fun submit(trainingFinalType: TrainingFinalType) {
        val trainingData = createTrainingData(trainingFinalType)
        setTrainingDataUseCase.execute(trainingData)
    }

    private fun createTrainingData(trainingFinalType: TrainingFinalType): TrainingData {
        return when (trainingFinalType) {
            TrainingFinalType.TEMPO_INCREASING_BY_BARS -> TrainingData.TempoIncreasing.ByBars(
                _adjustersValues[TEMPO_INCREASING_START_BPM]!!.value,
                _adjustersValues[TEMPO_INCREASING_END_BPM]!!.value,
                _adjustersValues[TEMPO_INCREASING_BY_BARS_EVERY_BARS]!!.value,
                _adjustersValues[TEMPO_INCREASING_BY_BARS_INCREASE_ON]!!.value
            )
            TrainingFinalType.TEMPO_INCREASING_BY_TIME -> TrainingData.TempoIncreasing.ByTime(
                _adjustersValues[TEMPO_INCREASING_START_BPM]!!.value,
                _adjustersValues[TEMPO_INCREASING_END_BPM]!!.value,
                _adjustersValues[TEMPO_INCREASING_BY_TIME_MINUTES]!!.value
            )
            TrainingFinalType.BAR_DROPPING_RANDOMLY -> TrainingData.BarDropping.Randomly(
                _adjustersValues[BAR_DROPPING_RANDOMLY_CHANCE]!!.value
            )
            TrainingFinalType.BAR_DROPPING_BY_VALUE -> TrainingData.BarDropping.ByValue(
                _adjustersValues[BAR_DROPPING_BY_VALUE_ORDINARY_BARS_COUNT]!!.value,
                _adjustersValues[BAR_DROPPING_BY_VALUE_MUTED_BARS_COUNT]!!.value
            )
            TrainingFinalType.BEAT_DROPPING -> TrainingData.BeatDropping(
                _adjustersValues[BEAT_DROPPING_CHANCE]!!.value
            )
        }
    }
}