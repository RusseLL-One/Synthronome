#include "Callback.h"

Callback::Callback(JavaVM *jvm, jobject listener) {
    this->jvm = jvm;
    this->listener = listener;
}

void Callback::invokeCallback(jint currentBpm) {
    if (listenerEnv == nullptr || listenerMethodId == nullptr) {
        listenerMethodId = getMethodId("onTick", "(I)V");
    }
    if (listenerEnv != nullptr && listenerMethodId != nullptr) {
        listenerEnv->CallVoidMethod(listener, listenerMethodId, currentBpm);
    }
}

jmethodID Callback::getMethodId(const char *name, const char *sig) {
    int status;
    bool isAttached = false;

    // Try to attach current thread
    status = jvm->GetEnv((void **) &listenerEnv, JNI_VERSION_1_6);
    if (status != JNI_OK) {
        status = jvm->AttachCurrentThread(&listenerEnv, nullptr);
        if (status != JNI_OK) {
            return nullptr;
        }

        isAttached = true;
    }

    jclass interfaceClass = listenerEnv->GetObjectClass(listener);
    if (!interfaceClass) {
        if (isAttached) jvm->DetachCurrentThread();
        return nullptr;
    }

    jmethodID methodId = listenerEnv->GetMethodID(interfaceClass, name, sig);
    if (!methodId) {
        if (isAttached) jvm->DetachCurrentThread();
        return nullptr;
    }
    if (isAttached) jvm->DetachCurrentThread();
    return methodId;
}
