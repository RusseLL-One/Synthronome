#include <android/asset_manager.h>
#include "ClickPlayer.h"

ClickPlayer::ClickPlayer(AAssetManager *assetManager, JavaVM *jvm, jobject listenerRef) {
    gJavaVM = jvm;
    listener = listenerRef;

    init();
}

void ClickPlayer::init() {

    oboe::AudioStreamBuilder builder;
    builder.setFormat(oboe::AudioFormat::I16)
    ->setDeviceId(oboe::kUnspecified)
    ->setSampleRate(kSampleRateHz)
    ->setChannelCount(1)
    ->setDirection(oboe::Direction::Output)
    ->setPerformanceMode(oboe::PerformanceMode::LowLatency)
    ->setSharingMode(oboe::SharingMode::Exclusive)
    ->setDataCallback(this);

    oboe::AudioStream *stream;
    oboe::Result result = builder.openStream(&stream);

    if (result == oboe::Result::OK) {
        stream->requestStart();
    }
}

oboe::DataCallbackResult
ClickPlayer::onAudioReady(oboe::AudioStream *audioStream, void *audioData, int32_t numFrames) {
    // todo
    return oboe::DataCallbackResult::Continue;
}