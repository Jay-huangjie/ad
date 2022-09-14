package com.zlfcapp.batterymanager.csj.provider

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import com.bytedance.sdk.openadsdk.*
import com.zlfcapp.batterymanager.core.listener.NativeListener
import com.zlfcapp.batterymanager.csj.TogetherAdCsj

/**
 *
 * Created by Matthew Chen on 2020/11/25.
 */
abstract class CsjProviderNative : CsjProviderInter() {

    override fun getNativeAdList(activity: FragmentActivity, adProviderType: String, alias: String, maxCount: Int, listener: NativeListener) {
        if (CsjProvider.Native.nativeAdType == -1) {
            throw IllegalArgumentException(
                    """
    |-------------------------------------------------------------------------------------- 
    |  必须在每次请求穿山甲的原生广告之前设置类型。
    |  设置方式：
    |  CsjProvider.Native.nativeAdType = AdSlot.TYPE_XXX（类型和你的广告位ID一致）。
    |  CsjProvider.Native.nativeAdType = AdSlot.TYPE_FEED
    |  CsjProvider.Native.nativeAdType = AdSlot.TYPE_INTERACTION_AD
    |  CsjProvider.Native.nativeAdType = AdSlot.TYPE_BANNER
    |  CsjProvider.Native.nativeAdType = AdSlot.TYPE_CACHED_SPLASH
    |  CsjProvider.Native.nativeAdType = AdSlot.TYPE_DRAW_FEED
    |  CsjProvider.Native.nativeAdType = AdSlot.TYPE_FULL_SCREEN_VIDEO
    |  CsjProvider.Native.nativeAdType = AdSlot.TYPE_REWARD_VIDEO
    |  CsjProvider.Native.nativeAdType = AdSlot.TYPE_SPLASH
    |  CsjProvider.Native.nativeAdType = AdSlot.TYPE_STREAM
    |  任选其一
    |--------------------------------------------------------------------------------------

"""
            )
        }

        //兼容穿山甲的 Stream 广告
        if (CsjProvider.Native.nativeAdType == AdSlot.TYPE_STREAM) {
            getStreamAdList(activity, adProviderType, alias, maxCount, listener)
            return
        }

        callbackNativeStartRequest(adProviderType, alias, listener)

        val adSlot = AdSlot.Builder()
                .setAdLoadType(TogetherAdCsj.adLoadType)
                .setCodeId(TogetherAdCsj.idMapCsj[alias])
                .setSupportDeepLink(CsjProvider.Native.supportDeepLink)
                .setImageAcceptedSize(CsjProvider.Native.imageAcceptedSizeWidth, CsjProvider.Native.imageAcceptedSizeHeight)
                .setNativeAdType(CsjProvider.Native.nativeAdType)
                .setAdCount(maxCount)
                .build()
        TogetherAdCsj.mTTAdManager.createAdNative(activity).loadNativeAd(adSlot, object : TTAdNative.NativeAdListener {
            override fun onNativeAdLoad(adList: MutableList<TTNativeAd>?) {
                if (adList.isNullOrEmpty()) {
                    callbackNativeFailed(adProviderType, alias, listener, null, "请求成功，但是返回的list为空")
                    return
                }

                callbackNativeLoaded(adProviderType, alias, listener, adList)
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                callbackNativeFailed(adProviderType, alias, listener, errorCode, errorMsg)
            }
        })
    }

    private fun getStreamAdList(activity: FragmentActivity, adProviderType: String, alias: String, maxCount: Int, listener: NativeListener) {

        callbackNativeStartRequest(adProviderType, alias, listener)

        val adSlot = AdSlot.Builder()
                .setAdLoadType(TogetherAdCsj.adLoadType)
                .setCodeId(TogetherAdCsj.idMapCsj[alias])
                .setImageAcceptedSize(CsjProvider.Native.imageAcceptedSizeWidth, CsjProvider.Native.imageAcceptedSizeHeight)
                .setAdCount(maxCount)
                .build()

        TogetherAdCsj.mTTAdManager.createAdNative(activity).loadStream(adSlot, object : TTAdNative.FeedAdListener {
            override fun onFeedAdLoad(adList: MutableList<TTFeedAd>?) {
                if (adList.isNullOrEmpty()) {
                    callbackNativeFailed(adProviderType, alias, listener, null, "请求成功，但是返回的list为空")
                    return
                }

                callbackNativeLoaded(adProviderType, alias, listener, adList)
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                callbackNativeFailed(adProviderType, alias, listener, errorCode, errorMsg)
            }
        })
    }

    override fun resumeNativeAd(adObject: Any) {
        when (adObject) {
            is TTNativeAd -> {

            }
        }
    }

    override fun pauseNativeAd(adObject: Any) {
        when (adObject) {
            is TTNativeAd -> {

            }
        }
    }

    override fun destroyNativeAd(adObject: Any) {
        when (adObject) {
            is TTNativeAd -> {
                adObject.destroy()
            }
        }
    }

    override fun nativeAdIsBelongTheProvider(adObject: Any): Boolean {
        return adObject is TTNativeAd
    }

}