package com.ifmvo.togetherad.huawei

import android.content.Context
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.huawei.provider.HuaweiProvider
import org.jetbrains.annotations.NotNull
import com.huawei.appgallery.agd.core.api.AgdAdApi
import com.huawei.appgallery.agd.core.api.AgdAdConfig
import com.huawei.appgallery.agd.core.api.InitCallback
import com.ifmvo.togetherad.core.entity.AdProviderEntity
import com.zlfcapp.ad.huawei.BuildConfig


object TogetherAdHw {

    var idMapKs = mutableMapOf<String, String>()
    //个性化开关
    var personalize:Int = 1

    fun init(
        @NotNull context: Context,
        @NotNull adProviderType: String,
    ) {
        init(context, adProviderType, null, null, object :InitCallback{
            override fun onInitSuccess() {
            }

            override fun onInitFail(p0: Int, p1: String?) {

            }

        })
    }

    fun init(
        @NotNull context: Context,
        @NotNull adProviderType: String,
        hwIdMap: Map<String, String>?
    ) {
        init(context, adProviderType, null, hwIdMap, object :InitCallback{
            override fun onInitSuccess() {
            }

            override fun onInitFail(p0: Int, p1: String?) {
            }

        })
    }

    fun init(
        @NotNull context: Context,
        @NotNull adProviderType: String,
        callback: InitCallback
    ) {
        init(context, adProviderType, null, null, callback)
    }

    fun init(
        @NotNull context: Context,
        @NotNull adProviderType: String,
        providerClassPath: String?,
        hwIdMap: Map<String, String>? = null,
        callback: InitCallback
    ) {
        com.ifmvo.togetherad.core.TogetherAd.addProvider(
            AdProviderEntity(
                adProviderType,
                if (providerClassPath?.isEmpty() != false) HuaweiProvider::class.java.name else providerClassPath
            )
        )
        hwIdMap?.let { idMapKs.putAll(it) }
        AgdAdApi.init(
            context,  // application context
            AgdAdConfig.Builder()
                .debug(BuildConfig.DEBUG)
                .build(), callback
        )
    }

}