#include "ClickPlayer.h"
#include "logging_macros.h"

ClickPlayer::ClickPlayer(AAssetManager *assetManager, JavaVM *jvm, jobject listener) {
    this->assetManager = assetManager;
    this->jvm = jvm;
    this->listener = listener;

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

void ClickPlayer::start() {
    isPlaying = true;
}

void ClickPlayer::stop() {
    isPlaying = false;
    currentFrame = 0;
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
}

void ClickPlayer::playRotateClick() {
    rotateClick->setPlaying(true);
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

void ClickPlayer::handleCallback() {
    int status;
    JNIEnv *env;
    bool isAttached = false;

    // Try to attach current thread
    status = jvm->GetEnv((void **) &env, JNI_VERSION_1_6);
    if (status != JNI_OK) {
        status = jvm->AttachCurrentThread(&env, nullptr);
        if (status != JNI_OK) {
            return;
        }

        isAttached = true;
    }

    jclass interfaceClass = env->GetObjectClass(listener);
    if (!interfaceClass) {
        if (isAttached) jvm->DetachCurrentThread();
        return;
    }

    jmethodID method = env->GetMethodID(interfaceClass, "onTick", "()V");
    if (!method) {
        if (isAttached) jvm->DetachCurrentThread();
        return;
    }
    env->CallVoidMethod(listener, method);
    if (isAttached) jvm->DetachCurrentThread();
}

oboe::DataCallbackResult
ClickPlayer::onAudioReady(oboe::AudioStream *audioStream, void *audioData, int32_t numFrames) {
    for (int i = 0; i < numFrames; ++i) {
        if (isPlaying) {
            if (currentFrame % framesInterval == 0) { //click!
                playBeat();
                if (currentBpm != newBpm) {
                    currentBpm = newBpm;

                    framesInterval = 60 * kSampleRateHz / currentBpm;
                }
                handleCallback();

                currentFrame = 0;
            }
            currentFrame++;
        }
        mMixer.renderAudio(static_cast<int16_t *>(audioData) + i, 1);
    }

    return oboe::DataCallbackResult::Continue;
}