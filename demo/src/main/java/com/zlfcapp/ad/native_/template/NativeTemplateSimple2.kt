package com.zlfcapp.ad.native_.template

import com.zlfcapp.ad.core.custom.native_.BaseNativeTemplate
import com.zlfcapp.ad.core.custom.native_.BaseNativeView
import com.zlfcapp.ad.csj.native_.view.NativeViewCsjSimple2
import com.zlfcapp.ad.app.AdProviderType
import com.zlfcapp.ad.gdt.native_.view.NativeViewGdtSimple2

/**
 *
 * Created by Matthew Chen on 2020/8/28.
 */
class NativeTemplateSimple2 : BaseNativeTemplate() {

    override fun getNativeView(adProviderType: String): BaseNativeView? {
        return when (adProviderType) {
            AdProviderType.GDT.type -> {
                NativeViewGdtSimple2()
            }
            AdProviderType.CSJ.type -> {
                NativeViewCsjSimple2()
            }
            else -> throw Exception("模板配置错误")
        }
    }
}