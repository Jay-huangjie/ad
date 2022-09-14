package com.zlfcapp.ad.native_.template

import com.ifmvo.togetherad.core.custom.native_.BaseNativeTemplate
import com.ifmvo.togetherad.core.custom.native_.BaseNativeView
import com.ifmvo.togetherad.csj.native_.view.NativeViewCsjSimple3
import com.zlfcapp.ad.app.AdProviderType
import com.ifmvo.togetherad.gdt.native_.view.NativeViewGdtSimple3

/*
 * Created by Matthew Chen on 2020-04-21.
 */
class NativeTemplateSimple3(onClose: ((adProviderType: String) -> Unit)? = null) : BaseNativeTemplate() {

    private var mOnClose = onClose

    override fun getNativeView(adProviderType: String): BaseNativeView? {
        return when (adProviderType) {
            AdProviderType.GDT.type -> {
                NativeViewGdtSimple3(mOnClose)
            }
            AdProviderType.CSJ.type -> {
                NativeViewCsjSimple3(mOnClose)
            }
            else -> throw Exception("模板配置错误")
        }
    }
}