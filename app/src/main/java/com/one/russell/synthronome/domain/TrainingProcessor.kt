package com.one.russell.synthronome.domain

import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import com.one.russell.synthronome.domain.providers.BeatTypesProvider
import com.one.russell.synthronome.domain.providers.BpmProvider
import com.one.russell.synthronome.domain.providers.TrainingStateProvider
import com.one.russell.synthronome.domain.wrappers.Clicker
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.job
import kotlin.coroutines.coroutineContext
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.random.Random

class TrainingProcessor(
    private val clicker: Clicker,
    private val bpmProvider: BpmProvider,
    private val beatTypesProvider: BeatTypesProvider,
    private val trainingStateProvider: TrainingStateProvider
) {
    private var runningJob: Job? = null
    private var percentageAnimator: ValueAnimator = ValueAnimator
        .ofInt(0, 100)
        .apply { interpolator = LinearInterpolator() }

    suspend fun startTraining(trainingData: TrainingData) {
        runningJob?.cancel()
        percentageAnimator.cancel()

        runningJob = coroutineContext.job

        val trainingTime = calcTrainingTime(trainingData = trainingData)
        if (trainingTime == TRAINING_TIME_INFINITE) {
            trainingStateProvider.trainingState.value = TrainingState.Running.Endless
        } else {
            startPercentageAnimator(trainingData)
        }

        when (trainingData) {
            is TrainingData.TempoIncreasing.ByBars ->
                processTempoIncreasingByBars(trainingData)
            is TrainingData.TempoIncreasing.ByTime ->
                processTempoIncreasingByTime(trainingData)
            is TrainingData.BarDropping.Randomly ->
                processBarDroppingRandomly(trainingData)
            is TrainingData.BarDropping.ByValue ->
                processBarDroppingByValue(trainingData)
            is TrainingData.BeatDropping ->
                processBeatDropping(trainingData)
        }
    }

    private fun startPercentageAnimator(trainingData: TrainingData) {
        percentageAnimator.duration = calcTrainingTime(trainingData = trainingData)
        percentageAnimator.addUpdateListener {
            val percentage = it.animatedValue as Int
            trainingStateProvider.trainingState.value = TrainingState.Running.Limited(percentage)
        }
        percentageAnimator.start()
    }

    private fun calcTrainingTime(trainingData: TrainingData): Long = trainingData.run {

        if (this !is TrainingData.TempoIncreasing) return@run TRAINING_TIME_INFINITE

        fun TrainingData.TempoIncreasing.calcBeatTime(increaseIndex: Int): Float {
            val isIncreasing = endBpm >= startBpm
            val bpm = if (isIncreasing)
                (startBpm + step * increaseIndex).coerceAtMost(endBpm)
            else
                (startBpm - step * increaseIndex).coerceAtLeast(endBpm)
            return 1000f * 60f / bpm
        }

        var totalTime = 0f
        val beatsInBar = beatTypesProvider.beatTypesFlow.value.size
        val bpmInterval = abs(endBpm - startBpm).toFloat()

        when (this) {
            is TrainingData.TempoIncreasing.ByBars -> {
                val increasesCount = ceil(bpmInterval / (everyBars * step)).toInt()
                for (increaseIndex in 0..increasesCount) {
                    val beatTimeMs = calcBeatTime(increaseIndex)
                    totalTime += beatTimeMs * beatsInBar * everyBars
                }
            }
            is TrainingData.TempoIncreasing.ByTime -> {
                val increasesCount = ceil(bpmInterval / step).toInt()
                for (increaseIndex in 0..increasesCount) {
                    val beatTimeMs = calcBeatTime(increaseIndex)
                    totalTime += (beatTimeMs * beatsInBar) * ceil((everySeconds * 1000f) / (beatTimeMs * beatsInBar))
                }
            }
        }

        return totalTime.toLong()
    }

    private suspend fun processTempoIncreasingByBars(
        trainingData: TrainingData.TempoIncreasing.ByBars
    ) {
        bpmProvider.bpmFlow.emit(trainingData.startBpm) // Send initial bpm value
        var barsPassed = 0

        clicker.onClicked.collectWithActionQueueHandler { click, aqh ->
            if (click.bpm == trainingData.endBpm && click.isNextBeatFirst)
                aqh.queueAction { stopTraining() }

            if (click.isNextBeatFirst) barsPassed++

            if (barsPassed == trainingData.everyBars) {
                val newBpm = calcNewBpm(click, trainingData)
                clicker.setBpm(newBpm)
                aqh.queueAction { bpmProvider.bpmFlow.emit(newBpm) }
                barsPassed = 0
            }
        }
    }

    private suspend fun processTempoIncreasingByTime(
        trainingData: TrainingData.TempoIncreasing.ByTime
    ) {
        bpmProvider.bpmFlow.emit(trainingData.startBpm) // Send initial bpm values
        var startTime = System.currentTimeMillis()
        var timePassed: Long

        clicker.onClicked.collectWithActionQueueHandler { click, aqh ->
            if (click.bpm == trainingData.endBpm && click.isNextBeatFirst)
                aqh.queueAction { stopTraining() }

            timePassed = System.currentTimeMillis() - startTime

            if (click.isNextBeatFirst && timePassed > trainingData.everySeconds * 1000L) {
                val newBpm = calcNewBpm(click, trainingData)
                clicker.setBpm(newBpm)
                aqh.queueAction { bpmProvider.bpmFlow.emit(newBpm) }
                startTime = System.currentTimeMillis()
                timePassed = 0L
            }
        }
    }

    private fun calcNewBpm(click: Clicker.Click, trainingData: TrainingData.TempoIncreasing): Int {
        return if (trainingData.startBpm < trainingData.endBpm) {
            (click.bpm + trainingData.step)
                .coerceIn(trainingData.startBpm, trainingData.endBpm)
        } else {
            (click.bpm - trainingData.step)
                .coerceIn(trainingData.endBpm, trainingData.startBpm)
        }
    }

    private suspend fun processBarDroppingRandomly(
        trainingData: TrainingData.BarDropping.Randomly
    ) {
        var isBarMuted = false
        clicker.onClicked.collect { click ->
            if (click.isNextBeatFirst) isBarMuted = isMuted(trainingData.chancePercent)
            if (isBarMuted) clicker.setNextBeatType(BeatType.MUTE)
        }
    }

    private suspend fun processBarDroppingByValue(
        trainingData: TrainingData.BarDropping.ByValue
    ) {
        var barsPassed = 0
        val ordinaryBarsCount = trainingData.ordinaryBarsCount
        val totalBarsCount = trainingData.ordinaryBarsCount + trainingData.mutedBarsCount
        clicker.onClicked.collect { click ->
            if (click.isNextBeatFirst) barsPassed++
            if (barsPassed == totalBarsCount) barsPassed = 0
            if (barsPassed >= ordinaryBarsCount) clicker.setNextBeatType(BeatType.MUTE)
        }
    }

    private suspend fun processBeatDropping(
        trainingData: TrainingData.BeatDropping
    ) {
        clicker.onClicked.collect {
            if (isMuted(trainingData.chancePercent))
                clicker.setNextBeatType(BeatType.MUTE)
        }
    }

    private fun isMuted(chancePercent: Int): Boolean {
        return Random.nextInt(101) <= chancePercent
    }

    fun stopTraining() {
        runningJob?.cancel()
        percentageAnimator.cancel()
        trainingStateProvider.trainingState.value = TrainingState.Idle
    }

    private suspend inline fun <T> Flow<T>.collectWithActionQueueHandler(crossinline action: suspend (value: T, aqh: ActionQueueHandler) -> Unit) {
        val actionQueueHandler = ActionQueueHandler()
        collect {
            actionQueueHandler.executeAll()
            action(it, actionQueueHandler)
        }
    }

    private class ActionQueueHandler {
        private val actionsList: MutableList<suspend () -> Unit> = mutableListOf()

        suspend fun executeAll() {
            actionsList.forEach { it() }
            actionsList.clear()
        }

        fun queueAction(action: suspend () -> Unit) {
            actionsList.add(action)
        }
    }

    companion object {
        const val TRAINING_TIME_INFINITE = -1L
    }
}