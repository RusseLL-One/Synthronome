package com.one.russell.synthronome.domain

sealed class TrainingData {

    sealed class TempoIncreasing(
        val startBpm: Int,
        val endBpm: Int
    ) : TrainingData() {

        class ByBars(
            startBpm: Int,
            endBpm: Int,
            val increaseOn: Int,
            val everyBars: Int
        ) : TempoIncreasing(startBpm, endBpm)

        class ByTime(
            startBpm: Int,
            endBpm: Int,
            val increaseOn: Int,
            val everySeconds: Int
        ) : TempoIncreasing(startBpm, endBpm)
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