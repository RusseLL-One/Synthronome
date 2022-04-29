package com.one.russell.metroman_20.domain.wrappers

import android.content.res.AssetManager
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.one.russell.metroman_20.domain.BeatType
import com.one.russell.metroman_20.domain.providers.BeatTypesProvider
import com.one.russell.metroman_20.domain.providers.BpmProvider
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@Suppress("FunctionName")
class Clicker(
    private val callback: ClickerCallback,
    private val beatTypesProvider: BeatTypesProvider,
    private val bpmProvider: BpmProvider,
    assetManager: AssetManager
) {

    private var beatIndex = -1
    private val nextBeatIndex
        get() = (beatIndex + 1) % beatTypes.size
    private var beatTypes: List<BeatType> = emptyList()

    private val _onClicked: MutableSharedFlow<Click> = MutableSharedFlow()
    val onClicked: SharedFlow<Click> = _onClicked

    init {
        native_init(callback, assetManager)

        ProcessLifecycleOwner.get().lifecycleScope.launch {
            callback.onClick.collect {
                incrementBeat()
                setNextBeatType(beatTypes[nextBeatIndex])
                _onClicked.emit(Click(it, beatIndex, beatTypes.size))
            }
        }
        ProcessLifecycleOwner.get().lifecycleScope.launch {
            beatTypesProvider.beatTypesFlow.collect {
                beatTypes = it
                setNextBeatType(beatTypes[nextBeatIndex])
            }
        }
        ProcessLifecycleOwner.get().lifecycleScope.launch {
            bpmProvider.bpmFlow.collect {
                setBpm(it)
            }
        }
    }

    private fun incrementBeat() {
        beatIndex = ++beatIndex % beatTypes.size
    }

    fun start(startBpm: Int?) {
        beatIndex = -1
        setNextBeatType(beatTypes[0])

        val bpm = startBpm ?: bpmProvider.bpmFlow.value
        native_start(bpm)
    }

    fun stop() {
        native_stop()
        beatIndex = -1
        setNextBeatType(beatTypes[0])
    }

    fun setNextBeatType(beatType: BeatType) {
        native_set_next_beat_type(beatType)
    }

    fun setBpm(bpm: Int) {
        native_set_bpm(bpm)
    }

    fun playRotateClick() {
        native_play_rotate_click()
    }

    private external fun native_init(listener: ClickerCallback, assetManager: AssetManager)
    private external fun native_start(startBpm: Int)
    private external fun native_stop()
    private external fun native_set_next_beat_type(beatType: BeatType)
    private external fun native_set_bpm(bpm: Int)
    private external fun native_play_rotate_click()

    class Click(
        val bpm: Int,
        private val beatIndex: Int,
        private val beatsInBar: Int
    ) {
        val isNextBeatFirst
            get() = beatIndex == beatsInBar - 1
    }
}