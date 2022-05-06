#ifndef SYNTHRONOME_CALLBACK_H
#define SYNTHRONOME_CALLBACK_H

#include <jni.h>

class Callback {

public:
    Callback(JavaVM *jvm, jobject listener);
    void invokeCallback(jint currentBpm);

private:
    jobject listener;
    JavaVM *jvm;
    JNIEnv *listenerEnv = nullptr;

    jmethodID listenerMethodId = nullptr;

    jmethodID getMethodId(const char *name, const char *sig);
};


#endif //SYNTHRONOME_CALLBACK_H
