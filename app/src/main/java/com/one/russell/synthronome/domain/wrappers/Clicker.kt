package com.one.russell.synthronome.domain.wrappers

import android.content.res.AssetManager
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.one.russell.synthronome.domain.BeatType
import com.one.russell.synthronome.domain.providers.BeatTypesProvider
import com.one.russell.synthronome.domain.providers.BpmProvider
import com.one.russell.synthronome.domain.providers.OptionsProvider
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@Suppress("FunctionName")
class Clicker(
    private val callback: ClickerCallback,
    private val vibrator: Vibrator,
    private val flasher: Flasher,
    private val optionsProvider: OptionsProvider,
    private val beatTypesProvider: BeatTypesProvider,
    private val bpmProvider: BpmProvider,
    assetManager: AssetManager
) {

    private var beatIndex = -1
    private val nextBeatIndex
        get() = (beatIndex + 1) % beatTypes.size
    private var beatTypes: List<BeatType> = emptyList()

    private val _onClicked: MutableSharedFlow<Click> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val onClicked: SharedFlow<Click> = _onClicked.asSharedFlow()

    init {
        native_init(callback, assetManager)

        ProcessLifecycleOwner.get().lifecycleScope.launch {
            callback.onClick.collect {
                incrementBeat()
                setNextBeatType(beatTypes[nextBeatIndex])
                if (beatIndex == 0) launch { vibrator.performVibrateIfEnabled() }
                if (beatIndex == 0) launch { flasher.performFlashIfEnabled() }
                _onClicked.emit(Click(it, beatIndex, beatTypes.size))
            }
        }
        ProcessLifecycleOwner.get().lifecycleScope.launch {
            beatTypesProvider.beatTypesFlow.collect {
                beatTypes = it
            }
        }
        ProcessLifecycleOwner.get().lifecycleScope.launch {
            bpmProvider.bpmFlow.collect {
                setBpm(it)
            }
        }
        ProcessLifecycleOwner.get().lifecycleScope.launch {
            optionsProvider.optionsFlow.collect {
                setSoundPreset(it.soundPresetId)
                vibrator.setEnabled(it.isVibrationEnabled)
                flasher.setEnabled(it.isFlashEnabled)
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

    fun setSoundPreset(id: Int) {
        native_set_sound_preset(id)
    }

    private external fun native_init(listener: ClickerCallback, assetManager: AssetManager)
    private external fun native_start(startBpm: Int)
    private external fun native_stop()
    private external fun native_set_next_beat_type(beatType: BeatType)
    private external fun native_set_bpm(bpm: Int)
    private external fun native_play_rotate_click()
    private external fun native_set_sound_preset(id: Int)

    class Click(
        val bpm: Int,
        private val beatIndex: Int,
        private val beatsInBar: Int
    ) {
        val isNextBeatFirst
            get() = beatIndex == beatsInBar - 1
    }
}