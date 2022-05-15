#include <jni.h>
#include <memory>
#include <oboe/Oboe.h>
#include <android/asset_manager_jni.h>
#include "ClickPlayer.h"
#include "BeatType.h"

extern "C" {
    #pragma clang diagnostic push
    #pragma ide diagnostic ignored "UnusedLocalVariable"

    std::unique_ptr <ClickPlayer> clickPlayer;

    JNIEXPORT void JNICALL
    Java_com_one_russell_synthronome_domain_wrappers_Clicker_native_1init(
        JNIEnv *env,
        jobject instance,
        jobject listener,
        jobject jAssetManager
    ) {
        JavaVM *jvm;
        env -> GetJavaVM(&jvm);
        jobject listenerRef = env->NewWeakGlobalRef(listener);
        AAssetManager *assetManager = AAssetManager_fromJava(env, jAssetManager);
        clickPlayer = std::make_unique<ClickPlayer>(assetManager, jvm, listenerRef);
    }

    JNIEXPORT void JNICALL
    Java_com_one_russell_synthronome_domain_wrappers_Clicker_native_1start(
        JNIEnv *env,
        jobject instance,
        jint startBpm
    ) {
        clickPlayer->start(startBpm);
    }

    JNIEXPORT void JNICALL
    Java_com_one_russell_synthronome_domain_wrappers_Clicker_native_1stop(
        JNIEnv *env,
        jobject instance
    ) {
        clickPlayer->stop();
    }

    JNIEXPORT void JNICALL
    Java_com_one_russell_synthronome_domain_wrappers_Clicker_native_1set_1next_1beat_1type(
            JNIEnv *env,
            jobject instance,
            jobject jBeatType
    ) {
        jclass BeatType_class = env->GetObjectClass(jBeatType);
        jmethodID BeatType_ordinal = env->GetMethodID(BeatType_class, "ordinal", "()I");
        auto ordinal = (int8_t)env->CallIntMethod(jBeatType, BeatType_ordinal);
        BeatType beatType;
        switch (ordinal) {
            case 0:
                beatType = MUTE;
                break;
            default:
            case 1:
                beatType = BEAT;
                break;
            case 2:
                beatType = SUBACCENT;
                break;
            case 3:
                beatType = ACCENT;
                break;
        }

        clickPlayer->setNextBeatType(beatType);
    }

    JNIEXPORT void JNICALL
    Java_com_one_russell_synthronome_domain_wrappers_Clicker_native_1set_1bpm(
        JNIEnv *pEnv,
        jobject pThis,
        jint bpm
    ) {
        clickPlayer->setBpm(bpm);
    }

    JNIEXPORT void JNICALL
    Java_com_one_russell_synthronome_domain_wrappers_Clicker_native_1play_1rotate_1click(
        JNIEnv *pEnv,
        jobject pThis
    ) {
        clickPlayer->playRotateClick();
    }

    JNIEXPORT void JNICALL
    Java_com_one_russell_synthronome_domain_wrappers_Clicker_native_1set_1sound_1preset(
        JNIEnv *pEnv,
        jobject pThis,
        jint id
    ) {
        clickPlayer->setSoundPreset(id);
    }

    #pragma clang diagnostic pop
}