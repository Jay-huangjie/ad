package com.zlfc.test.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle

/**
 * activity跳转管理工具类
 */
object ActivityHelperUtil {

    private const val BUNDLE_KEY:String = "bundle_key"

    /**
     * 启动新页面
     *
     * @param context
     * @param activity
     */
    fun startNewActivity(context: Context, activity: Class<*>) {
        val intent = Intent(context, activity)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    /**
     * 启动新页面带参数
     *
     * @param context
     * @param activity
     */
    fun startNewActivity(context: Context, activity: Class<*>, bundle: Bundle) {
        val intent = Intent(context, activity)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(BUNDLE_KEY, bundle)
        context.startActivity(intent)
    }

    /**
     * 启动新页面接受回调数据
     *
     * @param activity
     * @param clazz
     * @param bundle
     * @param requestCode
     */
    fun startNewActivity(activity: Activity, clazz: Class<*>, bundle: Bundle, requestCode: Int) {
        val intent = Intent(activity, clazz)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra(BUNDLE_KEY, bundle)
        activity.startActivityForResult(intent, requestCode)
    }


    /**
     * 启动新页面接受回调数据
     *
     * @param activity
     * @param clazz
     * @param requestCode
     */
    fun startNewActivity(activity: Activity, clazz: Class<*>, requestCode: Int) {
        val intent = Intent(activity, clazz)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.startActivityForResult(intent, requestCode)
    }

}