package com.zlfcapp.ad.native_.template

import com.zlfcapp.ad.core.custom.express.BaseNativeExpressTemplate
import com.zlfcapp.ad.core.custom.express.BaseNativeExpressView
import com.zlfcapp.ad.csj.native_.express.NativeExpressViewCsj
import com.zlfcapp.ad.app.AdProviderType
import com.zlfcapp.ad.gdt.native_.express.NativeExpressViewGdt

/**
 *
 * Created by Matthew Chen on 2020/11/27.
 */
class NativeExpressTemplateSimple : BaseNativeExpressTemplate() {

    override fun getNativeExpressView(adProviderType: String): BaseNativeExpressView? {
        return when (adProviderType) {
            AdProviderType.GDT.type -> {
                NativeExpressViewGdt()
            }
            AdProviderType.CSJ.type -> {
                NativeExpressViewCsj()
            }
            else -> {
                throw Exception("模板配置错误")
            }
        }
    }
}