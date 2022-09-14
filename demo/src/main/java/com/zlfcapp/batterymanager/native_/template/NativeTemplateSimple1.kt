package com.zlfcapp.batterymanager.native_.template

import com.zlfcapp.batterymanager.core.custom.native_.BaseNativeTemplate
import com.zlfcapp.batterymanager.core.custom.native_.BaseNativeView
import com.zlfcapp.batterymanager.csj.native_.view.NativeViewCsjSimple1
import com.zlfcapp.batterymanager.app.AdProviderType
import com.zlfcapp.batterymanager.gdt.native_.view.NativeViewGdtSimple1

/*
 * Created by Matthew Chen on 2020-04-21.
 */
class NativeTemplateSimple1 : BaseNativeTemplate() {

    override fun getNativeView(adProviderType: String): BaseNativeView? {
        return when (adProviderType) {
            AdProviderType.GDT.type -> {
                NativeViewGdtSimple1()
            }
            AdProviderType.CSJ.type -> {
                NativeViewCsjSimple1()
            }
            else -> throw Exception("模板配置错误")
        }
    }
}