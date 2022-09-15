package com.zlfcapp.ad.native_.template

import com.ifmvo.togetherad.core.custom.native_.BaseNativeTemplate
import com.ifmvo.togetherad.core.custom.native_.BaseNativeView
import com.ifmvo.togetherad.csj.native_.view.NativeViewCsjSimple2
import com.zlfcapp.ad.app.AdProviderType
import com.ifmvo.togetherad.gdt.native_.view.NativeViewGdtSimple2

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