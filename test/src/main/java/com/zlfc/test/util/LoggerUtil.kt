package com.zlfc.test.util

import android.util.Log
import java.util.*

object LoggerUtil {
    private var isDebug: Boolean = true

    /**
     * 打开或关闭debug模式
     * @param debug
     */
    fun openDebug(debug: Boolean) {
        isDebug = debug
    }

    /**
     * 生成tag
     * @param stackTraceElement
     */
    fun generateTag(stackTraceElement: StackTraceElement): String {
        var tag = "%s.%s(Line:%d)"//占位符
        var clazzName = stackTraceElement.className//类名
        try {
            clazzName?.let {
                clazzName = it.substring(it.lastIndexOf(".") + 1)
            }
            tag = String.format(
                Locale.ENGLISH,
                tag,
                clazzName,
                stackTraceElement.methodName,
                stackTraceElement.lineNumber
            )
//            tag = if (TextUtils.isEmpty(customTagPrefix)) tag else "$customTagPrefix:$tag"
        } catch (e: Exception) {
            tag = "LoggerUtil"
        }
        return tag
    }

    /**
     * 获取tag元素
     * @return
     */
    fun getStackTraceElement(): StackTraceElement {
        return Thread.currentThread().stackTrace[4]
    }

    /**
     * 打印error级别日志
     * @param content
     */
    fun e(content: String) {
        if (isDebug) {
            Log.e(generateTag(getStackTraceElement()), content)
        }
    }

    /**
     * 打印debug级别日志
     * @param content
     */
    fun d(content: String) {
        if (isDebug) {
            Log.d(generateTag(getStackTraceElement()), content)
        }
    }

    /**
     * 打印info级别日志
     * @param content
     */
    fun i(content: String) {
        if (isDebug) {
            Log.i(generateTag(getStackTraceElement()), content)
        }
    }

    /**
     * 打印warn级别日志
     * @param content
     */
    fun w(content: String) {
        if (isDebug) {
            Log.w(generateTag(getStackTraceElement()), content)
        }
    }

    /**
     * 打印verbose级别日志
     * @param content
     */
    fun v(content: String) {
        if (isDebug) {
            Log.v(generateTag(getStackTraceElement()), content)
        }
    }
}