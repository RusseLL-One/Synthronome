#include "ClickPlayer.h"
#include "logging_macros.h"
#include <thread>

ClickPlayer::ClickPlayer(AAssetManager *assetManager, JavaVM *jvm, jobject listener) {
    this->assetManager = assetManager;
    this->callback = new Callback(jvm, listener);

    initSounds();
    initOboe();
}

void ClickPlayer::initSounds() {
    rotateClick = new SoundRenderer(assetManager, "rotate_click.raw");
    clickSound = new SoundRenderer(assetManager, "beat1.raw");
    subAccentSound = new SoundRenderer(assetManager, "sub1.raw");
    accentSound = new SoundRenderer(assetManager, "acc1.raw");

    mMixer.addTrack(clickSound);
    mMixer.addTrack(subAccentSound);
    mMixer.addTrack(accentSound);
    mMixer.addTrack(rotateClick);
}

void ClickPlayer::initOboe() {

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

void ClickPlayer::start(jint startBpm) {
    setBpm(startBpm);
    currentFrame = 0;
    isPlaying = true;
}

void ClickPlayer::stop() {
    currentFrame = 0;
    isPlaying = false;
    newBpm = currentBpm;
}

void ClickPlayer::setNextBeatType(BeatType beatType) {
    nextBeatType = beatType;
}

void ClickPlayer::setSoundPreset(int8_t id) {
    char clickSoundName[10];
    char subAccentSoundName[10];
    char accentSoundName[10];

    sprintf(clickSoundName, "beat%d.raw", id);
    sprintf(subAccentSoundName, "sub%d.raw", id);
    sprintf(accentSoundName, "acc%d.raw", id);

    clickSound->loadAsset(assetManager, clickSoundName);
    subAccentSound->loadAsset(assetManager, subAccentSoundName);
    accentSound->loadAsset(assetManager, accentSoundName);
}

void ClickPlayer::setBpm(jint bpm) {
    this->newBpm = bpm;
    if (currentBpm == 0 || !isPlaying) {
        setCurrentBpm(bpm);
    }
}

void ClickPlayer::setCurrentBpm(jint bpm) {
    if (bpm == 0) return;
    this->currentBpm = bpm;
    this->framesInterval = 60 * kSampleRateHz / bpm;
}

void ClickPlayer::playRotateClick() {
    rotateClick->queuePlaying(true);
}

void ClickPlayer::playBeat() {
    switch (nextBeatType) {
        case MUTE:
            break;
        default:
        case BEAT:
            clickSound->setPlaying(true);
            break;
        case SUBACCENT:
            subAccentSound->setPlaying(true);
            break;
        case ACCENT:
            accentSound->setPlaying(true);
            break;
    }

    nextBeatType = BEAT;
}

oboe::DataCallbackResult
ClickPlayer::onAudioReady(oboe::AudioStream *audioStream, void *audioData, int32_t numFrames) {
    for (int i = 0; i < numFrames; ++i) {
        if (isPlaying) {
            if (currentFrame % framesInterval == 0) { //click!
                playBeat();
                if (currentBpm != newBpm) {
                    setCurrentBpm(newBpm);
                }
                callback->invokeCallback(currentBpm);

                currentFrame = 0;
            }
            currentFrame++;
        }
        mMixer.renderAudio(static_cast<int16_t *>(audioData) + i, 1);
    }

    return oboe::DataCallbackResult::Continue;
}