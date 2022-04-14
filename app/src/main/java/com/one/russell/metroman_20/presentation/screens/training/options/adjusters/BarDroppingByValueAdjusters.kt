package com.one.russell.metroman_20.presentation.screens.training.options.adjusters

import com.one.russell.metroman_20.R
import com.one.russell.metroman_20.domain.TrainingData
import com.one.russell.metroman_20.domain.TrainingFinalType
import com.one.russell.metroman_20.presentation.screens.training.options.OptionsAdjusterType
import com.one.russell.metroman_20.presentation.screens.training.options.OptionsAdjusterType.*
import com.one.russell.metroman_20.presentation.screens.training.options.adapter.ListItem
import kotlinx.coroutines.flow.MutableStateFlow

class BarDroppingByValueAdjusters(doOnKnobRotation: () -> Unit) : AdjustersSet(doOnKnobRotation) {

    private val ordinaryBarsCountStateFlow = MutableStateFlow(0)
    private val mutedBarsCountStateFlow = MutableStateFlow(0)

    override fun OptionsAdjusterType.getStateFlow(): MutableStateFlow<Int> {
        return when (this) {
            BAR_DROPPING_BY_VALUE_ORDINARY_BARS_COUNT -> ordinaryBarsCountStateFlow
            BAR_DROPPING_BY_VALUE_MUTED_BARS_COUNT -> mutedBarsCountStateFlow
            else -> throw IllegalArgumentException()
        }
    }

    override suspend fun initValues(receiveSavedData: suspend (OptionsAdjusterType) -> Int) {
        ordinaryBarsCountStateFlow.value = receiveSavedData.invoke(BAR_DROPPING_BY_VALUE_ORDINARY_BARS_COUNT)
        mutedBarsCountStateFlow.value = receiveSavedData.invoke(BAR_DROPPING_BY_VALUE_MUTED_BARS_COUNT)
    }

    override fun createAdjustersList(trainingFinalType: TrainingFinalType): List<ListItem> {
        return listOf(
            createPickerItem(BAR_DROPPING_BY_VALUE_ORDINARY_BARS_COUNT, R.string.training_barDropping_ordinaryBarsCount),
            createPickerItem(BAR_DROPPING_BY_VALUE_MUTED_BARS_COUNT, R.string.training_barDropping_mutedBarsCount)
        )
    }

    override fun createTrainingData(trainingFinalType: TrainingFinalType): TrainingData {
        return when (trainingFinalType) {
            TrainingFinalType.BAR_DROPPING_BY_VALUE -> TrainingData.BarDropping.ByValue(
                BAR_DROPPING_BY_VALUE_ORDINARY_BARS_COUNT.getStateFlow().value,
                BAR_DROPPING_BY_VALUE_MUTED_BARS_COUNT.getStateFlow().value
            )
            else -> throw IllegalArgumentException()
        }
    }
}