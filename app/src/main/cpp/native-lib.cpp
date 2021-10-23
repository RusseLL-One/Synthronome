#include <jni.h>
#include <memory>
#include <oboe/Oboe.h>
#include <android/asset_manager_jni.h>
#include "ClickPlayer.h"

extern "C" {

    std::unique_ptr <ClickPlayer> clickPlayer;

    JNIEXPORT void JNICALL
    Java_com_one_russell_metroman_120_MainActivity_native_1init(
        JNIEnv *env,
        jobject instance,
        jobject jAssetManager
    ) {
        JavaVM *jvm;
        env -> GetJavaVM(&jvm);
        jobject listenerRef = env->NewGlobalRef(instance);
        AAssetManager *assetManager = AAssetManager_fromJava(env, jAssetManager);
        clickPlayer = std::make_unique<ClickPlayer>(assetManager, jvm, listenerRef);
    }

}