package com.zlfc.test.util

import android.content.Context
import android.view.Gravity
import android.widget.Toast

/**
@ClassName:      ToastUtil
@Description:     toast提示工具类
@Author:           edz
@CreateDate:     2020/7/31 10:31
@Version:        1.0
 */
object ToastUtil {

    /**
     * 短时间显示Toast 默认在中间
     * @param context
     * @param text
     */
    fun showToast(context: Context, text: String) {
        var toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }


    /**
     * 短时间显示Toast 默认在中间
     * @param context
     * @param text
     */
    fun showToast(context: Context, text: Int) {
        var toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    /**
     * 自定义显示Toast时间 默认在中间
     * @param context
     * @param text
     */
    fun showToast(context: Context, text: String, duration: Int) {
        var toast = Toast.makeText(context, text, duration)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    /**
     * 自定义显示Toast时间 默认在中间
     * @param context
     * @param text
     */
    fun showToast(context: Context, text: Int, duration: Int) {
        var toast = Toast.makeText(context, text, duration)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    /**
     * 短时间显示Toast 在底部
     * @param context
     * @param text
     */
    fun showToastBottom(context: Context, text: String) {
        var toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
        toast.show()
    }

    /**
     * 短时间显示Toast 在底部
     * @param context
     * @param text
     */
    fun showToastBottom(context: Context, text: Int) {
        var toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
        toast.show()
    }

    /**
     * 长时间显示Toast 默认在中间
     * @param context
     * @param text
     */
    fun showToastLong(context: Context, text: String) {
        var toast = Toast.makeText(context, text, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    /**
     * 长时间显示Toast 默认在中间
     * @param context
     * @param text
     */
    fun showToastLong(context: Context, text: Int) {
        var toast = Toast.makeText(context, text, Toast.LENGTH_LONG)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    /**
     * 长时间显示Toast 在底部
     * @param context
     * @param text
     */
    fun showToastBottomLong(context: Context, text: String) {
        var toast = Toast.makeText(context, text, Toast.LENGTH_LONG)
        toast.show()
    }

    /**
     * 长时间显示Toast 在底部
     * @param context
     * @param text
     */
    fun showToastBottomLong(context: Context, text: Int) {
        var toast = Toast.makeText(context, text, Toast.LENGTH_LONG)
        toast.show()
    }

    /**
     * 自定义显示Toast时间 默认在中间
     * @param context
     * @param text
     * @param duration
     */
    fun showToastBottom(context: Context, text: String, duration: Int) {
        var toast = Toast.makeText(context, text, duration)
        toast.show()
    }

    /**
     * 自定义显示Toast时间 默认在中间
     * @param context
     * @param text
     * @param duration
     */
    fun showToastBottom(context: Context, text: Int, duration: Int) {
        var toast = Toast.makeText(context, text, duration)
        toast.show()
    }

    /**
     * 显示自定义toast
     */
    /* fun showCustomToastCenter(context: Context, text: String, icon: Int) {
         var inflater = LayoutInflater.from(context)
         var view = inflater.inflate(R.layout.custom_toast_view, null)
         var ll = view.findViewById<LinearLayout>(R.id.custom_toast_view_ll)
         var contentLl = view.findViewById<LinearLayout>(R.id.custom_toast_view_content_ll)
         var param = LinearLayout.LayoutParams(
             DensityUtil.dip2px(context, 150F),
             LinearLayout.LayoutParams.WRAP_CONTENT
         )
         contentLl.layoutParams = param
         ShadowDrawableUtil.setShadowDrawable(
             ll,
             Color.parseColor("#FFFFFF"),
             DensityUtil.dip2px(context, 6F),
             Color.parseColor("#0F000000"),
             DensityUtil.dip2px(context, 7F),
             0,
             DensityUtil.dip2px(context, 4F)
         )
         var iv = view.findViewById<ImageView>(R.id.custom_toast_view_iv)
         var tv = view.findViewById<TextView>(R.id.custom_toast_view_tv)
         iv?.setImageResource(icon)
         tv?.text = text
         var toast = Toast.makeText(context, text, Toast.LENGTH_SHORT)
         toast.view = view
         toast.setGravity(Gravity.CENTER, 0, 0)
         toast.show()
     }*/
}