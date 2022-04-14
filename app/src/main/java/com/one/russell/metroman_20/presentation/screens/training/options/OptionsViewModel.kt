package com.one.russell.metroman_20.presentation.screens.training.options

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.one.russell.metroman_20.domain.TrainingFinalType
import com.one.russell.metroman_20.domain.usecases.training.GetTrainingDataUseCase
import com.one.russell.metroman_20.domain.usecases.training.SetTrainingDataUseCase
import com.one.russell.metroman_20.domain.wrappers.Clicker
import com.one.russell.metroman_20.presentation.screens.training.options.adapter.ListItem
import com.one.russell.metroman_20.presentation.screens.training.options.adjusters.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class OptionsViewModel(
    private val clicker: Clicker,
    private val setTrainingDataUseCase: SetTrainingDataUseCase,
    private val getTrainingDataUseCase: GetTrainingDataUseCase
) : ViewModel() {

    private lateinit var adjustersSet: AdjustersSet

    private var _adjustersList = MutableStateFlow<List<ListItem>>(emptyList())
    val adjustersList: StateFlow<List<ListItem>>
        get() = _adjustersList

    fun createAdjustersList(trainingType: TrainingFinalType) {
        adjustersSet = getAdjustersSet(trainingType)

        viewModelScope.launch {
            adjustersSet.initValues(getTrainingDataUseCase::execute)
            _adjustersList.emit(adjustersSet.createAdjustersList(trainingType))
        }
    }

    private fun getAdjustersSet(trainingFinalType: TrainingFinalType): AdjustersSet {
        val doOnKnobRotation = { clicker.playRotateClick() }
        return when (trainingFinalType) {
            TrainingFinalType.TEMPO_INCREASING_BY_BARS -> TempoIncreasingByBarsAdjusters(doOnKnobRotation)
            TrainingFinalType.TEMPO_INCREASING_BY_TIME -> TempoIncreasingByTimeAdjusters(doOnKnobRotation)
            TrainingFinalType.BAR_DROPPING_RANDOMLY -> BarDroppingRandomlyAdjusters(doOnKnobRotation)
            TrainingFinalType.BAR_DROPPING_BY_VALUE -> BarDroppingByValueAdjusters(doOnKnobRotation)
            TrainingFinalType.BEAT_DROPPING -> BeatDroppingAdjusters(doOnKnobRotation)
        }
    }

    suspend fun submit(trainingFinalType: TrainingFinalType) {
        val trainingData = adjustersSet.createTrainingData(trainingFinalType)
        setTrainingDataUseCase.execute(trainingData)
    }
}