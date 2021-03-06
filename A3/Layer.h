/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class Layer */

#ifndef _Included_Layer
#define _Included_Layer
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     Layer
 * Method:    create
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_Layer_create
  (JNIEnv *, jobject, jint);

/*
 * Class:     Layer
 * Method:    destroy
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_Layer_destroy
  (JNIEnv *, jobject);

/*
 * Class:     Layer
 * Method:    parseFrom
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_Layer_parseFrom
  (JNIEnv *, jobject, jstring);

/*
 * Class:     Layer
 * Method:    manage
 * Signature: (Ljava/lang/String;II)Z
 */
JNIEXPORT jboolean JNICALL Java_Layer_manage
  (JNIEnv *, jobject, jstring, jint, jint);

/*
 * Class:     Layer
 * Method:    hasValue
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_Layer_hasValue
  (JNIEnv *, jobject, jstring);

/*
 * Class:     Layer
 * Method:    getFloat
 * Signature: (Ljava/lang/String;)F
 */
JNIEXPORT jfloat JNICALL Java_Layer_getFloat
  (JNIEnv *, jobject, jstring);

/*
 * Class:     Layer
 * Method:    getBool
 * Signature: (Ljava/lang/String;)Z
 */
JNIEXPORT jboolean JNICALL Java_Layer_getBool
  (JNIEnv *, jobject, jstring);

/*
 * Class:     Layer
 * Method:    getStr
 * Signature: (Ljava/lang/String;)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_Layer_getStr
  (JNIEnv *, jobject, jstring);

/*
 * Class:     Layer
 * Method:    getList
 * Signature: (Ljava/lang/String;)[Ljava/lang/String;
 */
JNIEXPORT jobjectArray JNICALL Java_Layer_getList
  (JNIEnv *, jobject, jstring);

/*
 * Class:     Layer
 * Method:    getInt
 * Signature: (Ljava/lang/String;)I
 */
JNIEXPORT jint JNICALL Java_Layer_getInt
  (JNIEnv *, jobject, jstring);

#ifdef __cplusplus
}
#endif
#endif
