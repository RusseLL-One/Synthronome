#ifndef METROMAN_CLICKPLAYER_H
#define METROMAN_CLICKPLAYER_H
#include <oboe/Oboe.h>
#include <jni.h>

class ClickPlayer : public oboe::AudioStreamCallback {
public:
    ClickPlayer(AAssetManager *assetManager, JavaVM *jvm, jobject listenerRef);

    void init();

    oboe::DataCallbackResult
    onAudioReady(oboe::AudioStream *audioStream, void *audioData, int32_t numFrames);

private:
    JavaVM *gJavaVM;
    jobject listener;

    const int kSampleRateHz = 44100;
};

#endif //METROMAN_CLICKPLAYER_H
