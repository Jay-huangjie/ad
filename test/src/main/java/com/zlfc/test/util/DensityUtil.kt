package com.zlfc.test.util

import android.content.Context

/**
@ClassName:      DensityUtil
@Description:     手机密度工具类
@Author:           edz
@CreateDate:     2020/7/29 9:30
@Version:        1.0
 */
object DensityUtil {

    /**
     * 获取屏幕密度
     */
    fun getDensity(context: Context):Float{
        return context.resources.displayMetrics.density
    }


    /**
     * dp转换成px
     * @param context
     * @param dipValue
     */
    fun dip2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }

    /**
     * px转换成dp
     * @param context
     * @param pxValue
     */
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * sp转换成px
     * @param context
     * @param spValue
     */
    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**
     * px转换成sp
     * @param context
     * @param pxValue
     */
    fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    /**
     * 获取屏幕宽度
     * @param context
     * @return width 单位dp
     */
    fun getScreenWidth(context: Context):Int{
        var dm = context.resources.displayMetrics
        return px2dip(context,dm.widthPixels.toFloat())
    }

    /**
     * 获取屏幕高度
     * @param context
     * @return height 单位dp
     */
    fun getScreenHeight(context: Context):Int{
        var dm = context.resources.displayMetrics
        return px2dip(context,dm.heightPixels.toFloat())
    }


}