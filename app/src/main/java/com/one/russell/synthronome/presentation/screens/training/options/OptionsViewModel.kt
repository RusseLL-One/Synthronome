package com.one.russell.synthronome.presentation.screens.training.options

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.one.russell.synthronome.domain.TrainingData
import com.one.russell.synthronome.domain.TrainingFinalType
import com.one.russell.synthronome.domain.usecases.PlayRotateClickUseCase
import com.one.russell.synthronome.domain.usecases.colors.ObserveColorsUseCase
import com.one.russell.synthronome.domain.usecases.training.RestoreTrainingDataUseCase
import com.one.russell.synthronome.domain.usecases.training.SaveTrainingDataUseCase
import com.one.russell.synthronome.domain.usecases.training.StartTrainingUseCase
import com.one.russell.synthronome.presentation.screens.training.options.OptionsAdjusterType.*
import com.one.russell.synthronome.presentation.screens.training.options.adapter.AdjusterListItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OptionsViewModel(
    private val playRotateClickUseCase: PlayRotateClickUseCase,
    private val saveTrainingDataUseCase: SaveTrainingDataUseCase,
    private val restoreTrainingDataUseCase: RestoreTrainingDataUseCase,
    private val startTrainingUseCase: StartTrainingUseCase,
    private val observeColorsUseCase: ObserveColorsUseCase
) : ViewModel() {

    val colors get() = observeColorsUseCase.execute().value

    private var adjustersFlows: Map<OptionsAdjusterType, MutableStateFlow<Int>> = emptyMap()

    private var _adjustersListItems = MutableStateFlow<List<AdjusterListItem>>(emptyList())
    val adjustersListItems get() = _adjustersListItems.asStateFlow()

    fun createAdjustersList(trainingFinalType: TrainingFinalType) {
        viewModelScope.launch {
            adjustersFlows = createTypesList(trainingFinalType).associateWith {
                MutableStateFlow(restoreTrainingDataUseCase.execute(it))
            }
            _adjustersListItems.value = adjustersFlows.map {
                it.key.toListItem()
            }
        }
    }

    fun setListItemValue(type: OptionsAdjusterType, value: Int) {
        fun AdjusterListItem.mutateItem(): AdjusterListItem {
            return when (this.type.controlType) {
                ControlType.KNOB -> {
                    playRotateClickUseCase.execute()
                    // value for knob is delta, so we need to calculate the value
                    val newValue = (type.getFlowValue() + value)
                        .coerceIn(this.type.minValue, this.type.maxValue)

                    type.setFlowValue(newValue)
                    this.copy(value = newValue)
                }
                ControlType.PICKER -> {
                    type.setFlowValue(value)
                    this // displayed picker value is handled by view holder
                }
            }
        }

        val newItemsList = _adjustersListItems.value.map {
            if (it.type == type) it.mutateItem()
            else it
        }

        _adjustersListItems.value = newItemsList
    }

    fun submit(trainingFinalType: TrainingFinalType) {
        val trainingData = createTrainingData(trainingFinalType)
        startTrainingUseCase.execute(trainingData)
        viewModelScope.launch {
            saveTrainingDataUseCase.execute(trainingData)
        }
    }

    private fun OptionsAdjusterType.toListItem() = AdjusterListItem(
        type = this,
        value = getFlowValue(),
        colorPrimary = colors.colorPrimary,
        colorSecondary = colors.colorSecondary,
        colorOnBackground = colors.colorOnBackground
    )

    private fun OptionsAdjusterType.getFlowValue(): Int = adjustersFlows[this]!!.value

    private fun OptionsAdjusterType.setFlowValue(value: Int) {
        adjustersFlows[this]!!.value = value
    }

    private fun createTypesList(trainingFinalType: TrainingFinalType): List<OptionsAdjusterType> =
        when (trainingFinalType) {
            TrainingFinalType.TEMPO_INCREASING_BY_BARS -> listOf(
                TEMPO_INCREASING_START_BPM,
                TEMPO_INCREASING_END_BPM,
                TEMPO_INCREASING_INCREASE_ON,
                TEMPO_INCREASING_BY_BARS_EVERY_BARS
            )
            TrainingFinalType.TEMPO_INCREASING_BY_TIME -> listOf(
                TEMPO_INCREASING_START_BPM,
                TEMPO_INCREASING_END_BPM,
                TEMPO_INCREASING_INCREASE_ON,
                TEMPO_INCREASING_BY_TIME_EVERY_SECONDS
            )
            TrainingFinalType.BAR_DROPPING_RANDOMLY -> listOf(
                BAR_DROPPING_RANDOMLY_CHANCE
            )
            TrainingFinalType.BAR_DROPPING_BY_VALUE -> listOf(
                BAR_DROPPING_BY_VALUE_ORDINARY_BARS_COUNT,
                BAR_DROPPING_BY_VALUE_MUTED_BARS_COUNT
            )
            TrainingFinalType.BEAT_DROPPING -> listOf(
                BEAT_DROPPING_CHANCE
            )
        }

    private fun createTrainingData(trainingFinalType: TrainingFinalType): TrainingData {
        return when (trainingFinalType) {
            TrainingFinalType.TEMPO_INCREASING_BY_BARS -> TrainingData.TempoIncreasing.ByBars(
                TEMPO_INCREASING_START_BPM.getFlowValue(),
                TEMPO_INCREASING_END_BPM.getFlowValue(),
                TEMPO_INCREASING_INCREASE_ON.getFlowValue(),
                TEMPO_INCREASING_BY_BARS_EVERY_BARS.getFlowValue()
            )
            TrainingFinalType.TEMPO_INCREASING_BY_TIME -> TrainingData.TempoIncreasing.ByTime(
                TEMPO_INCREASING_START_BPM.getFlowValue(),
                TEMPO_INCREASING_END_BPM.getFlowValue(),
                TEMPO_INCREASING_INCREASE_ON.getFlowValue(),
                TEMPO_INCREASING_BY_TIME_EVERY_SECONDS.getFlowValue()
            )
            TrainingFinalType.BAR_DROPPING_RANDOMLY -> TrainingData.BarDropping.Randomly(
                BAR_DROPPING_RANDOMLY_CHANCE.getFlowValue()
            )
            TrainingFinalType.BAR_DROPPING_BY_VALUE -> TrainingData.BarDropping.ByValue(
                BAR_DROPPING_BY_VALUE_ORDINARY_BARS_COUNT.getFlowValue(),
                BAR_DROPPING_BY_VALUE_MUTED_BARS_COUNT.getFlowValue()
            )
            TrainingFinalType.BEAT_DROPPING -> TrainingData.BeatDropping(
                BEAT_DROPPING_CHANCE.getFlowValue()
            )
        }
    }
}