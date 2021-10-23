#ifndef METROMAN_CLICKPLAYER_H
#define METROMAN_CLICKPLAYER_H
#include <oboe/Oboe.h>
#include <jni.h>
#include <android/asset_manager.h>
#include "SoundRenderer.h"
#include "Mixer.h"
#include "BeatType.h"

class ClickPlayer : public oboe::AudioStreamCallback {
public:
    ClickPlayer(AAssetManager *assetManager, JavaVM *jvm, jobject listener);

    void start();
    void stop();

    void setNextBeatType(BeatType beatType);

    void setSoundPreset(int8_t id);
    void setBpm(jint bpm);

    void playRotateClick();

    void handleCallback();

    oboe::DataCallbackResult
    onAudioReady(oboe::AudioStream *audioStream, void *audioData, int32_t numFrames);

private:
    void initSounds();
    void initOboe();
    void playBeat();

    JavaVM *jvm;
    AAssetManager *assetManager;
    jobject listener;

    const int kSampleRateHz = 44100;

    Mixer mMixer;
    SoundRenderer *accentSound{nullptr};
    SoundRenderer *subAccentSound{nullptr};
    SoundRenderer *clickSound{nullptr};
    SoundRenderer *rotateClick{nullptr};

    jint currentBpm = 0;
    jint newBpm = 500;

    int64_t currentFrame = 0;
    int64_t framesInterval = 0;

    BeatType nextBeatType = BEAT;

    bool isPlaying = false;
};

#endif //METROMAN_CLICKPLAYER_H
