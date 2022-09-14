package com.zlfcapp.batterymanager.native_.template

import com.zlfcapp.batterymanager.core.custom.native_.BaseNativeTemplate
import com.zlfcapp.batterymanager.core.custom.native_.BaseNativeView
import com.zlfcapp.batterymanager.csj.native_.view.NativeViewCsjSimple4
import com.zlfcapp.batterymanager.app.AdProviderType
import com.zlfcapp.batterymanager.gdt.native_.view.NativeViewGdtSimple4

/*
 * Created by Matthew Chen on 2020-04-21.
 */
class NativeTemplateSimple4(onClose: ((adProviderType: String) -> Unit)? = null) : BaseNativeTemplate() {

    private var mOnClose = onClose

    override fun getNativeView(adProviderType: String): BaseNativeView? {
        return when (adProviderType) {
            AdProviderType.GDT.type -> {
                NativeViewGdtSimple4(mOnClose)
            }
            AdProviderType.CSJ.type -> {
                NativeViewCsjSimple4(mOnClose)
            }
            else -> throw Exception("模板配置错误")
        }
    }
}