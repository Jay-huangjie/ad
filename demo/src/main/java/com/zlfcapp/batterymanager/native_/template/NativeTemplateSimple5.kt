package com.zlfcapp.batterymanager.native_.template

import com.zlfcapp.batterymanager.core.custom.native_.BaseNativeTemplate
import com.zlfcapp.batterymanager.core.custom.native_.BaseNativeView
import com.zlfcapp.batterymanager.csj.native_.view.NativeViewCsjSimple5
import com.zlfcapp.batterymanager.app.AdProviderType
import com.zlfcapp.batterymanager.gdt.native_.view.NativeViewGdtSimple5

/*
 * Created by Matthew Chen on 2020-04-21.
 */
class NativeTemplateSimple5(onClose: ((providerType: String) -> Unit)? = null) : BaseNativeTemplate() {

    private var mOnClose = onClose

    override fun getNativeView(adProviderType: String): BaseNativeView? {
        return when (adProviderType) {
            AdProviderType.GDT.type -> {
                NativeViewGdtSimple5(mOnClose)
            }
            AdProviderType.CSJ.type -> {
                NativeViewCsjSimple5(mOnClose)
            }
            else -> throw Exception("模板配置错误")
        }
    }
}