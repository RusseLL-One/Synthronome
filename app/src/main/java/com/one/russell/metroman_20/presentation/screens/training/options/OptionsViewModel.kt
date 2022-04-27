package com.one.russell.metroman_20.presentation.screens.training.options

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.one.russell.metroman_20.domain.Colors
import com.one.russell.metroman_20.domain.TrainingData
import com.one.russell.metroman_20.domain.TrainingFinalType
import com.one.russell.metroman_20.domain.usecases.PlayRotateClickUseCase
import com.one.russell.metroman_20.domain.usecases.colors.ObserveColorsUseCase
import com.one.russell.metroman_20.domain.usecases.training.RestoreTrainingDataUseCase
import com.one.russell.metroman_20.domain.usecases.training.SaveTrainingDataUseCase
import com.one.russell.metroman_20.presentation.screens.training.options.OptionsAdjusterType.*
import com.one.russell.metroman_20.presentation.screens.training.options.adapter.AdjusterListItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OptionsViewModel(
    private val playRotateClickUseCase: PlayRotateClickUseCase,
    private val saveTrainingDataUseCase: SaveTrainingDataUseCase,
    private val restoreTrainingDataUseCase: RestoreTrainingDataUseCase,
    private val observeColorsUseCase: ObserveColorsUseCase
) : ViewModel() {

    val colors: Colors get() = observeColorsUseCase.execute().value

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
                    type.setFlowValue(value)
                    this.copy(value = value)
                }
                ControlType.PICKER -> this // displayed picker value is handled by view holder
            }
        }

        val newItemsList = _adjustersListItems.value.map {
            if (it.type == type) it.mutateItem()
            else it
        }

        _adjustersListItems.value = newItemsList
    }

    suspend fun submit(trainingFinalType: TrainingFinalType) {
        val trainingData = createTrainingData(trainingFinalType)
        saveTrainingDataUseCase.execute(trainingData)
    }

    private fun OptionsAdjusterType.toListItem() = AdjusterListItem(
        type = this,
        value = getFlowValue(),
        primaryColor = colors.primaryColor,
        secondaryColor = colors.secondaryColor
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
                TEMPO_INCREASING_BY_BARS_EVERY_BARS,
                TEMPO_INCREASING_BY_BARS_INCREASE_ON
            )
            TrainingFinalType.TEMPO_INCREASING_BY_TIME -> listOf(
                TEMPO_INCREASING_START_BPM,
                TEMPO_INCREASING_END_BPM,
                TEMPO_INCREASING_BY_TIME_MINUTES
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
                TEMPO_INCREASING_BY_BARS_EVERY_BARS.getFlowValue(),
                TEMPO_INCREASING_BY_BARS_INCREASE_ON.getFlowValue()
            )
            TrainingFinalType.TEMPO_INCREASING_BY_TIME -> TrainingData.TempoIncreasing.ByTime(
                TEMPO_INCREASING_START_BPM.getFlowValue(),
                TEMPO_INCREASING_END_BPM.getFlowValue(),
                TEMPO_INCREASING_BY_TIME_MINUTES.getFlowValue()
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