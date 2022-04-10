package com.one.russell.metroman_20.domain

import com.one.russell.metroman_20.domain.wrappers.Clicker
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.job
import kotlin.coroutines.coroutineContext
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.random.Random

class TrainingProcessor(
    private val clicker: Clicker
) {
    private val _trainingState = MutableStateFlow<TrainingState>(TrainingState.Idle)
    val trainingState: StateFlow<TrainingState>
        get() = _trainingState

    private var runningJob: Job? = null

    suspend fun startTraining(trainingData: TrainingData, bpmFlow: MutableSharedFlow<Int>) {
        this.runningJob?.cancel()
        this.runningJob = coroutineContext.job

        _trainingState.emit(TrainingState.Running(
            calcTrainingTime(trainingData),
            trainingData is TrainingData.TempoIncreasing
        ))

        when (trainingData) {
            is TrainingData.TempoIncreasing.ByBars -> {
                processTempoIncreasingByBars(bpmFlow, trainingData)
            }
            is TrainingData.TempoIncreasing.ByTime -> {
                processTempoIncreasingByTime(bpmFlow, trainingData)
            }
            is TrainingData.BarDropping.Randomly -> {
                processBarDroppingRandomly(trainingData)
            }
            is TrainingData.BarDropping.ByValue -> {
                processBarDroppingByValue(trainingData)
            }
            is TrainingData.BeatDropping -> {
                processBeatDropping(trainingData)
            }
        }
    }

    private fun calcTrainingTime(trainingData: TrainingData): Long {
        return when (trainingData) {
            is TrainingData.TempoIncreasing.ByBars -> {
                var totalTime = 0L
                val beatsInBar = 4 // todo

                val totalIncreases = abs(ceil((trainingData.endBpm - trainingData.startBpm).toFloat() / (trainingData.everyBars * trainingData.increaseOn))).toInt()
                for (i in 0..totalIncreases) {
                    val beatTimeMs = 1000 * 60 / (trainingData.startBpm + trainingData.increaseOn * i)
                    totalTime += beatTimeMs * beatsInBar * trainingData.everyBars
                }
                totalTime
            }
            is TrainingData.TempoIncreasing.ByTime -> trainingData.minutes * 60L * 1000L
            else -> TRAINING_TIME_INFINITE
        }
    }

    private suspend inline fun processTempoIncreasingByBars(
        bpmFlow: MutableSharedFlow<Int>,
        trainingData: TrainingData.TempoIncreasing.ByBars
    ) {
        // Send initial bpm values
        bpmFlow.emit(trainingData.startBpm)
        clicker.setBpm(trainingData.startBpm)

        var barsPassed = 0
        var completeOnNextBar = false

        clicker.onClicked.collect { click ->
            if (completeOnNextBar && click.isFirstBeat) stopTraining()
            if (click.bpm == trainingData.endBpm) completeOnNextBar = true

            if (barsPassed == 0) {
                bpmFlow.emit(click.bpm)
            }
            if (click.isNextBeatFirst) {
                barsPassed++
            }
            if (barsPassed == trainingData.everyBars) {
                val newBpm = if (trainingData.startBpm < trainingData.endBpm) {
                    (click.bpm + trainingData.increaseOn).coerceIn(
                        trainingData.startBpm,
                        trainingData.endBpm
                    )
                } else {
                    (click.bpm - trainingData.increaseOn).coerceIn(
                        trainingData.endBpm,
                        trainingData.startBpm
                    )
                }

                clicker.setBpm(newBpm)
                barsPassed = 0
            }
        }
    }

    private suspend inline fun processTempoIncreasingByTime(
        bpmFlow: MutableSharedFlow<Int>,
        trainingData: TrainingData.TempoIncreasing.ByTime
    ) {
        // Send initial bpm values
        bpmFlow.emit(trainingData.startBpm)
        clicker.setBpm(trainingData.startBpm)

        val duration = trainingData.minutes * 60 * 1000
        val startTime = System.currentTimeMillis()

        clicker.onClicked.collect {
            bpmFlow.emit(it.bpm)
            if (it.bpm == trainingData.endBpm) stopTraining()

            val currentTime = System.currentTimeMillis()

            val completion = (currentTime + (1000 * 60 / it.bpm) - startTime).toFloat() / duration
            val bpmIncrease = (trainingData.endBpm - trainingData.startBpm) * completion
            val newBpm = (trainingData.startBpm + bpmIncrease).toInt()

            clicker.setBpm(newBpm)
        }
    }

    private suspend inline fun processBarDroppingRandomly(
        trainingData: TrainingData.BarDropping.Randomly
    ) {
        var isMuted = Random.nextInt(101) <= trainingData.chancePercent
        if (isMuted) {
            clicker.setNextBeatType(BeatType.MUTE)
        }

        clicker.onClicked.collect { click ->
            if (click.isNextBeatFirst) {
                isMuted = Random.nextInt(101) <= trainingData.chancePercent
            }

            if (isMuted) {
                clicker.setNextBeatType(BeatType.MUTE)
            }
        }
    }

    private suspend inline fun processBarDroppingByValue(
        trainingData: TrainingData.BarDropping.ByValue
    ) {
        var barsPassed = 0

        clicker.onClicked.collect { click ->
            if (click.isNextBeatFirst) {
                barsPassed++
            }
            if (barsPassed >= trainingData.ordinaryBarsCount + trainingData.mutedBarsCount) {
                barsPassed = 0
            } else if (barsPassed >= trainingData.ordinaryBarsCount) {
                clicker.setNextBeatType(BeatType.MUTE)
            }
        }
    }

    private suspend inline fun processBeatDropping(
        trainingData: TrainingData.BeatDropping
    ) {
        var isMuted = Random.nextInt(101) <= trainingData.chancePercent
        if (isMuted) {
            clicker.setNextBeatType(BeatType.MUTE)
        }

        clicker.onClicked.collect {
            isMuted = Random.nextInt(101) <= trainingData.chancePercent

            if (isMuted) {
                clicker.setNextBeatType(BeatType.MUTE)
            }
        }
    }

    fun stopTraining() {
        runningJob?.cancel()
        _trainingState.tryEmit(TrainingState.Idle)
    }

    companion object {
        const val TRAINING_TIME_INFINITE = -1L
    }
}