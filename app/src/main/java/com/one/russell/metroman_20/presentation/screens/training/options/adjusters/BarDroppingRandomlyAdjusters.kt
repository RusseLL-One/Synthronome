package com.one.russell.metroman_20.presentation.screens.training.options.adjusters

import com.one.russell.metroman_20.R
import com.one.russell.metroman_20.domain.TrainingData
import com.one.russell.metroman_20.domain.TrainingFinalType
import com.one.russell.metroman_20.presentation.screens.training.options.OptionsAdjusterType
import com.one.russell.metroman_20.presentation.screens.training.options.OptionsAdjusterType.*
import com.one.russell.metroman_20.presentation.screens.training.options.adapter.ListItem
import kotlinx.coroutines.flow.MutableStateFlow

class BarDroppingRandomlyAdjusters(doOnKnobRotation: () -> Unit) : AdjustersSet(doOnKnobRotation) {

    private val chanceStateFlow = MutableStateFlow(0)

    override fun OptionsAdjusterType.getStateFlow(): MutableStateFlow<Int> {
        return when (this) {
            BAR_DROPPING_RANDOMLY_CHANCE -> chanceStateFlow
            else -> throw IllegalArgumentException()
        }
    }

    override suspend fun initValues(receiveSavedData: suspend (OptionsAdjusterType) -> Int) {
        chanceStateFlow.value = receiveSavedData.invoke(BAR_DROPPING_RANDOMLY_CHANCE)
    }

    override fun createAdjustersList(trainingFinalType: TrainingFinalType): List<ListItem> {
        return listOf(
            createPickerItem(BAR_DROPPING_RANDOMLY_CHANCE, R.string.training_barDropping_chance)
        )
    }

    override fun createTrainingData(trainingFinalType: TrainingFinalType): TrainingData {
        return when (trainingFinalType) {
            TrainingFinalType.BAR_DROPPING_RANDOMLY -> TrainingData.BarDropping.Randomly(
                BAR_DROPPING_RANDOMLY_CHANCE.getStateFlow().value
            )
            else -> throw IllegalArgumentException()
        }
    }
}