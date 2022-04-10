package com.one.russell.metroman_20.domain

sealed class TrainingData {

    sealed class TempoIncreasing(
        val startBpm: Int,
        val endBpm: Int
    ) : TrainingData() {

        class ByBars(
            startBpm: Int,
            endBpm: Int,
            val everyBars: Int,
            val increaseOn: Int
        ) : TempoIncreasing(startBpm, endBpm)

        class ByTime(
            startBpm: Int,
            endBpm: Int,
            val minutes: Int
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