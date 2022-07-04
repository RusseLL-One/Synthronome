package com.one.russell.synthronome.domain

sealed class TrainingData {

    sealed class TempoChange(
        val startBpm: Int,
        val endBpm: Int,
        val step: Int
    ) : TrainingData() {

        class ByBars(
            startBpm: Int,
            endBpm: Int,
            step: Int,
            val everyBars: Int
        ) : TempoChange(startBpm, endBpm, step)

        class ByTime(
            startBpm: Int,
            endBpm: Int,
            step: Int,
            val everySeconds: Int
        ) : TempoChange(startBpm, endBpm, step)
    }

    sealed class BarDropping : TrainingData() {

        class Randomly(
            val chancePercent: Int
        ) : BarDropping()

        class ByValue(
            val ordinaryBarsCount: Int,
            val mutedBarsCount: Int,
        ) : BarDropping()
    }

    class BeatDropping(
        val chancePercent: Int
    ) : TrainingData()
}