package com.one.russell.metroman_20.presentation.screens.training.options.adjusters

import com.one.russell.metroman_20.R
import com.one.russell.metroman_20.domain.TrainingData
import com.one.russell.metroman_20.domain.TrainingFinalType
import com.one.russell.metroman_20.presentation.screens.training.options.OptionsAdjusterType
import com.one.russell.metroman_20.presentation.screens.training.options.OptionsAdjusterType.*
import com.one.russell.metroman_20.presentation.screens.training.options.adapter.ListItem
import kotlinx.coroutines.flow.MutableStateFlow

class TempoIncreasingByTimeAdjusters(doOnKnobRotation: () -> Unit) : AdjustersSet(doOnKnobRotation) {

    private val startBpmStateFlow = MutableStateFlow(0)
    private val endBpmStateFlow = MutableStateFlow(0)
    private val minutesStateFlow = MutableStateFlow(0)

    override fun OptionsAdjusterType.getStateFlow(): MutableStateFlow<Int> {
        return when (this) {
            TEMPO_INCREASING_START_BPM -> startBpmStateFlow
            TEMPO_INCREASING_END_BPM -> endBpmStateFlow
            TEMPO_INCREASING_BY_TIME_MINUTES -> minutesStateFlow
            else -> throw IllegalArgumentException()
        }
    }

    override suspend fun initValues(receiveSavedData: suspend (OptionsAdjusterType) -> Int) {
        startBpmStateFlow.value = receiveSavedData.invoke(TEMPO_INCREASING_START_BPM)
        endBpmStateFlow.value = receiveSavedData.invoke(TEMPO_INCREASING_END_BPM)
        minutesStateFlow.value = receiveSavedData.invoke(TEMPO_INCREASING_BY_TIME_MINUTES)
    }

    override fun createAdjustersList(trainingFinalType: TrainingFinalType): List<ListItem> {
        return listOf(
            createKnobItem(TEMPO_INCREASING_START_BPM, R.string.training_tempoIncreasing_startBpm),
            createKnobItem(TEMPO_INCREASING_END_BPM, R.string.training_tempoIncreasing_endBpm),
            createPickerItem(TEMPO_INCREASING_BY_TIME_MINUTES, R.string.training_tempoIncreasing_minutes)
        )
    }

    override fun createTrainingData(trainingFinalType: TrainingFinalType): TrainingData {
        return when (trainingFinalType) {
            TrainingFinalType.TEMPO_INCREASING_BY_TIME -> TrainingData.TempoIncreasing.ByTime(
                TEMPO_INCREASING_START_BPM.getStateFlow().value,
                TEMPO_INCREASING_END_BPM.getStateFlow().value,
                TEMPO_INCREASING_BY_TIME_MINUTES.getStateFlow().value
            )
            else -> throw IllegalArgumentException()
        }
    }
}