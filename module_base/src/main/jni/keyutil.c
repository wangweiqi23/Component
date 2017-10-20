#include "com_weiqi_modulebase_util_KeyUtil.h"

JNIEXPORT jstring JNICALL Java_com_weiqi_modulebase_util_KeyUtil_getAlipayKey(JNIEnv *env, jobject obj) {
    return (*env)->NewStringUTF(env, "alipay:123");
}