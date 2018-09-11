#include "stdafx.h"
#include <stdlib.h>
#include "com_mrhan_console_Console.h"
#include "com_mrhan_console_ConsoleInputStream.h"
#include "com_mrhan_console_ConsoleOutputStream.h"

JNIEXPORT jlong JNICALL Java_com_mrhan_console_Console_createDos(JNIEnv *evn, jobject obj) {
	jlong j = 100;
	return j;
}
JNIEXPORT jint JNICALL Java_com_mrhan_console_ConsoleInputStream_read
(JNIEnv * evn, jobject obj, jlong jlong) {
	return 0;
}
void JNICALL Java_com_mrhan_console_ConsoleOutputStream_write(JNIEnv * evn, jobject obj, jlong lon) 
{
	system("pause");
	printf("111");
}

