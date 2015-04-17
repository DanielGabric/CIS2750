/*Name: Daniel Gabric
  USER ID: ******
  Assignment #: 3
*/
#include <jni.h>
#include "ParameterManager.h"
#include <stdlib.h>
#include "Layer.h"

ParameterManager *p;


JNIEXPORT void JNICALL Java_Layer_create(JNIEnv * env, jobject obj, jint size)
{
    p = PM_create(size);
}

JNIEXPORT void JNICALL Java_Layer_destroy(JNIEnv * env, jobject obj)
{
    if(!p)
    {
        return;
    }
    PM_destroy(p);
}

JNIEXPORT jboolean JNICALL Java_Layer_parseFrom(JNIEnv * env, jobject obj, jstring path)
{
    const char *mystring = (*env)->GetStringUTFChars(env, path, 0);
    /*0 is parse error
    1 is not parse error
    set to parse error initially*/
    int isParseError;
    isParseError = PARSE_ERROR;

    FILE * fp = fopen(mystring,"r");
    if(fp)
    {
        isParseError = PM_parseFrom(p,fp,'#');
    }
    fclose(fp);
    (*env)->ReleaseStringUTFChars(env, path, mystring);
    return isParseError;
}

JNIEXPORT jboolean JNICALL Java_Layer_manage(JNIEnv * env, jobject obj, jstring pname, jint ptype, jint required)
{
    const char *mystring = (*env)->GetStringUTFChars(env, pname, 0);
    char * stringToPass;
    param_t type;
    int req = (int)required;
    int isGood = 0;
    switch(ptype)
    {
        case 0:
            type = INT_TYPE;
            break;
        case 1:
            type = REAL_TYPE;
            break;
        case 2:
            type = BOOLEAN_TYPE;
            break;
        case 3:
            type = STRING_TYPE;
            break;
        case 4:
            type = LIST_TYPE;
            break;
    }

    stringToPass = malloc(sizeof(char)*(strlen(mystring)+1));
    strcpy(stringToPass,mystring);
    if(stringToPass)
    {
        isGood = PM_manage(p,stringToPass,type,req);
        free(stringToPass);
    }
    (*env)->ReleaseStringUTFChars(env, pname, mystring);
    return isGood;
}

JNIEXPORT jfloat JNICALL Java_Layer_getFloat(JNIEnv * env, jobject obj, jstring pname)
{
    const char *mystring = (*env)->GetStringUTFChars(env, pname, 0);
    char * copiedString;
    jfloat floatValue;
    copiedString = malloc(sizeof(char)*(strlen(mystring)+1));
    strcpy(copiedString,mystring);
    floatValue = (float)PM_getValue(p,copiedString).real_val;
    free(copiedString);
    (*env)->ReleaseStringUTFChars(env, pname, mystring);
    return floatValue;
}

JNIEXPORT jboolean JNICALL Java_Layer_getBool(JNIEnv * env, jobject obj, jstring pname)
{
    const char *mystring = (*env)->GetStringUTFChars(env, pname, 0);
    char * copiedString;
    jboolean boolValue;
    copiedString = malloc(sizeof(char)*(strlen(mystring)+1));
    strcpy(copiedString,mystring);
    boolValue = PM_getValue(p,copiedString).bool_val==false?JNI_FALSE:JNI_TRUE;
    free(copiedString);
    (*env)->ReleaseStringUTFChars(env, pname, mystring);
    return boolValue;
}

JNIEXPORT jint JNICALL Java_Layer_getInt(JNIEnv * env, jobject obj, jstring pname)
{
    const char *mystring = (*env)->GetStringUTFChars(env, pname, 0);
    char * copiedString;
    jint integerValue;
    copiedString = malloc(sizeof(char)*(strlen(mystring)+1));
    strcpy(copiedString,mystring);
    integerValue = PM_getValue(p,copiedString).int_val;
    free(copiedString);
    (*env)->ReleaseStringUTFChars(env, pname, mystring);
    return integerValue;
}

JNIEXPORT jstring JNICALL Java_Layer_getStr(JNIEnv * env, jobject obj, jstring pname)
{
    const char *mystring = (*env)->GetStringUTFChars(env, pname, 0);
    char * copiedString;
    jstring stringValue;
    copiedString = malloc(sizeof(char)*(strlen(mystring)+1));
    strcpy(copiedString,mystring);
    stringValue = (*env)->NewStringUTF(env,PM_getValue(p,copiedString).str_val);
    free(copiedString);
    (*env)->ReleaseStringUTFChars(env, pname, mystring);
    return stringValue;
}


/*Still need to do*/
JNIEXPORT jobjectArray JNICALL Java_Layer_getList(JNIEnv * env, jobject obj, jstring pname)
{
    const char *mystring = (*env)->GetStringUTFChars(env, pname, 0);
    jclass stringClass = (*env)->FindClass(env,"java/lang/String");
    char * copiedString;
    int size;
    copiedString = malloc(sizeof(char)*(strlen(mystring)+1));
    strcpy(copiedString,mystring);
    ParameterList * pl = PM_getValue(p,copiedString).list_val;
    size = pl->size;
    jobjectArray list = (jobjectArray)(*env)->NewObjectArray(env,size-1,stringClass,0);
    int i=0;
    char * plnext;
    while((plnext = PL_next(pl)))
    {
        jstring newString = (jstring)(*env)->NewStringUTF(env,plnext);
        (*env)->SetObjectArrayElement(env,list,i++,newString);
    }
    free(copiedString);
    (*env)->ReleaseStringUTFChars(env, pname, mystring);
    return list;
}

JNIEXPORT jboolean JNICALL Java_Layer_hasValue(JNIEnv * env, jobject obj, jstring pname)
{
    const char *mystring = (*env)->GetStringUTFChars(env, pname, 0);
    jboolean hasValue = JNI_FALSE;
    char * copiedString;
    copiedString = malloc(sizeof(char)*(strlen(mystring)+1));
    strcpy(copiedString,mystring);
    hasValue = PM_hasValue(p,copiedString);
    free(copiedString);
    (*env)->ReleaseStringUTFChars(env, pname, mystring);
    return hasValue;
}
