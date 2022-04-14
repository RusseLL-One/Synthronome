package com.one.russell.metroman_20.presentation.screens.training.options.adjusters

import androidx.annotation.StringRes
import com.one.russell.metroman_20.domain.TrainingData
import com.one.russell.metroman_20.domain.TrainingFinalType
import com.one.russell.metroman_20.presentation.screens.training.options.OptionsAdjusterType
import com.one.russell.metroman_20.presentation.screens.training.options.adapter.KnobItem
import com.one.russell.metroman_20.presentation.screens.training.options.adapter.ListItem
import com.one.russell.metroman_20.presentation.screens.training.options.adapter.PickerItem
import kotlinx.coroutines.flow.MutableStateFlow

abstract class AdjustersSet(private val doOnKnobRotation: () -> Unit) {
    abstract suspend fun initValues(receiveSavedData: suspend (OptionsAdjusterType) -> Int)
    abstract fun OptionsAdjusterType.getStateFlow(): MutableStateFlow<Int>
    abstract fun createAdjustersList(trainingFinalType: TrainingFinalType): List<ListItem>
    abstract fun createTrainingData(trainingFinalType: TrainingFinalType): TrainingData

    protected fun createKnobItem(type: OptionsAdjusterType, @StringRes titleRes: Int): KnobItem {
        return KnobItem(titleRes, type.getStateFlow()) {
            val newValue = (type.getStateFlow().value + it).coerceIn(type.minValue, type.maxValue)
            type.getStateFlow().value = newValue
            doOnKnobRotation.invoke()
        }
    }

    protected fun createPickerItem(type: OptionsAdjusterType, @StringRes titleRes: Int): PickerItem {
        return PickerItem(type.minValue, type.maxValue, type.step, titleRes, type.getStateFlow().value) {
            type.getStateFlow().value = it
        }
    }
}