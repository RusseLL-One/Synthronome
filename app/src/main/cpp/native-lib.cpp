#include <jni.h>
#include <memory>
#include <oboe/Oboe.h>
#include <android/asset_manager_jni.h>
#include "ClickPlayer.h"

extern "C" {

    std::unique_ptr <ClickPlayer> clickPlayer;

    JNIEXPORT void JNICALL
    Java_com_one_russell_metroman_120_domain_wrappers_Clicker_native_1init(
        JNIEnv *env,
        jobject instance,
        jobject jAssetManager
    ) {
        JavaVM *jvm;
        env -> GetJavaVM(&jvm);
        jobject listener = env->NewGlobalRef(instance);
        AAssetManager *assetManager = AAssetManager_fromJava(env, jAssetManager);
        clickPlayer = std::make_unique<ClickPlayer>(assetManager, jvm, listener);
    }

    JNIEXPORT void JNICALL
    Java_com_one_russell_metroman_120_domain_wrappers_Clicker_native_1start(
        JNIEnv *env,
        jobject instance
    ) {
        clickPlayer->start();
    }

    JNIEXPORT void JNICALL
    Java_com_one_russell_metroman_120_domain_wrappers_Clicker_native_1stop(
        JNIEnv *env,
        jobject instance
    ) {
        clickPlayer->stop();
    }

    JNIEXPORT void JNICALL
    Java_com_one_russell_metroman_120_domain_wrappers_Clicker_native_1set_1bpm(
        JNIEnv *pEnv,
        jobject pThis,
        int16_t bpm
    ) {
        clickPlayer->setBpm(bpm);
    }

    JNIEXPORT void JNICALL
    Java_com_one_russell_metroman_120_domain_wrappers_Clicker_native_1play_1rotate_1click(
        JNIEnv *pEnv,
        jobject pThis
    ) {
        clickPlayer->playRotateClick();
    }
}