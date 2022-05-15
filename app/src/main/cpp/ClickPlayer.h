#ifndef SYNTHRONOME_CLICKPLAYER_H
#define SYNTHRONOME_CLICKPLAYER_H
#include <oboe/Oboe.h>
#include <jni.h>
#include <android/asset_manager.h>
#include "SoundRenderer.h"
#include "Mixer.h"
#include "BeatType.h"
#include "Callback.h"

class ClickPlayer : public oboe::AudioStreamCallback {
public:
    ClickPlayer(AAssetManager *assetManager, JavaVM *jvm, jobject listener);

    void start(jint startBpm);
    void stop();

    void setNextBeatType(BeatType beatType);

    void setSoundPreset(jint id);
    void setBpm(jint bpm);

    void playRotateClick();

    oboe::DataCallbackResult
    onAudioReady(oboe::AudioStream *audioStream, void *audioData, int32_t numFrames);

private:
    void initSounds();
    void initOboe();
    void playBeat();
    void setCurrentBpm(jint bpm);

    AAssetManager *assetManager;

    Callback *callback{nullptr};

    const int kSampleRateHz = 44100;

    Mixer mMixer;
    SoundRenderer *accentSound{nullptr};
    SoundRenderer *subAccentSound{nullptr};
    SoundRenderer *clickSound{nullptr};
    SoundRenderer *rotateClick{nullptr};

    jint currentBpm = 0;
    jint newBpm = 500;

    int64_t currentFrame = 1;
    int64_t framesInterval = 1;

    BeatType nextBeatType = BEAT;

    bool isPlaying = false;
};

#endif //SYNTHRONOME_CLICKPLAYER_H
