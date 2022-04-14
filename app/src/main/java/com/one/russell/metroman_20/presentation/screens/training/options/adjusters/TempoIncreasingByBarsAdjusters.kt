package com.one.russell.metroman_20.presentation.screens.training.options.adjusters

import com.one.russell.metroman_20.R
import com.one.russell.metroman_20.domain.TrainingData
import com.one.russell.metroman_20.domain.TrainingFinalType
import com.one.russell.metroman_20.presentation.screens.training.options.OptionsAdjusterType
import com.one.russell.metroman_20.presentation.screens.training.options.OptionsAdjusterType.*
import com.one.russell.metroman_20.presentation.screens.training.options.adapter.ListItem
import kotlinx.coroutines.flow.MutableStateFlow

class TempoIncreasingByBarsAdjusters(doOnKnobRotation: () -> Unit) : AdjustersSet(doOnKnobRotation) {

    private val startBpmStateFlow = MutableStateFlow(0)
    private val endBpmStateFlow = MutableStateFlow(0)
    private val everyBarsStateFlow = MutableStateFlow(0)
    private val increaseOnStateFlow = MutableStateFlow(0)

    override fun OptionsAdjusterType.getStateFlow(): MutableStateFlow<Int> {
        return when (this) {
            TEMPO_INCREASING_START_BPM -> startBpmStateFlow
            TEMPO_INCREASING_END_BPM -> endBpmStateFlow
            TEMPO_INCREASING_BY_BARS_EVERY_BARS -> everyBarsStateFlow
            TEMPO_INCREASING_BY_BARS_INCREASE_ON -> increaseOnStateFlow
            else -> throw IllegalArgumentException()
        }
    }

    override suspend fun initValues(receiveSavedData: suspend (OptionsAdjusterType) -> Int) {
        startBpmStateFlow.value = receiveSavedData.invoke(TEMPO_INCREASING_START_BPM)
        endBpmStateFlow.value = receiveSavedData.invoke(TEMPO_INCREASING_END_BPM)
        everyBarsStateFlow.value = receiveSavedData.invoke(TEMPO_INCREASING_BY_BARS_EVERY_BARS)
        increaseOnStateFlow.value = receiveSavedData.invoke(TEMPO_INCREASING_BY_BARS_INCREASE_ON)
    }

    override fun createAdjustersList(trainingFinalType: TrainingFinalType): List<ListItem> {
        return listOf(
            createKnobItem(TEMPO_INCREASING_START_BPM, R.string.training_tempoIncreasing_startBpm),
            createKnobItem(TEMPO_INCREASING_END_BPM, R.string.training_tempoIncreasing_endBpm),
            createPickerItem(TEMPO_INCREASING_BY_BARS_EVERY_BARS, R.string.training_tempoIncreasing_everyBars),
            createKnobItem(TEMPO_INCREASING_BY_BARS_INCREASE_ON, R.string.training_tempoIncreasing_increaseOn)
        )
    }

    override fun createTrainingData(trainingFinalType: TrainingFinalType): TrainingData {
        return when (trainingFinalType) {
            TrainingFinalType.TEMPO_INCREASING_BY_BARS -> TrainingData.TempoIncreasing.ByBars(
                TEMPO_INCREASING_START_BPM.getStateFlow().value,
                TEMPO_INCREASING_END_BPM.getStateFlow().value,
                TEMPO_INCREASING_BY_BARS_EVERY_BARS.getStateFlow().value,
                TEMPO_INCREASING_BY_BARS_INCREASE_ON.getStateFlow().value
            )
            else -> throw IllegalArgumentException()
        }
    }
}