package com.zlfcapp.batterymanager.csj.native_.view

import android.view.ViewGroup
import com.zlfcapp.batterymanager.core.listener.NativeViewListener
import com.zlfcapp.batterymanager.core.utils.ScreenUtil

/**
 * Created by Matthew Chen on 2020-04-21.
 */
class NativeViewCsjSimple5(onClose: ((adProviderType: String) -> Unit)? = null) : BaseNativeViewCsj(onClose){

    override fun showNative(adProviderType: String, adObject: Any, container: ViewGroup, listener: NativeViewListener?) {
        super.showNative(adProviderType, adObject, container, listener)

        getImageContainer()?.layoutParams?.height = (ScreenUtil.getDisplayMetricsWidth(container.context) * 9 / 16)
        getVideoContainer()?.layoutParams?.height = (ScreenUtil.getDisplayMetricsWidth(container.context) * 9 / 16)
    }

}