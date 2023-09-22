package com.ifmvo.togetherad.csj.provider

import android.content.Context
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.bytedance.sdk.openadsdk.*
import com.ifmvo.togetherad.core.listener.SplashListener
import com.ifmvo.togetherad.csj.TogetherAdCsj

/**
 *
 * Created by Matthew Chen on 2020/11/25.
 */
abstract class CsjProviderSplash : CsjProviderReward() {

    private var mListener: SplashListener? = null
    private var mAdProviderType: String? = null


    private var mSplashAd: CSJSplashAd? = null
    override fun loadOnlySplashAd(
        activity: FragmentActivity,
        adProviderType: String,
        alias: String,
        listener: SplashListener
    ) {

        mListener = listener
        mAdProviderType = adProviderType

        callbackSplashStartRequest(adProviderType, alias, listener)

        val adSlotBuilder = AdSlot.Builder()
        adSlotBuilder.setCodeId(TogetherAdCsj.idMapCsj[alias])
        adSlotBuilder.setAdLoadType(TogetherAdCsj.adLoadType)
        adSlotBuilder.setExpressViewAcceptedSize(
            CsjProvider.Splash.imageAcceptedSizeWidthDp,
            CsjProvider.Splash.imageAcceptedSizeHeightDp
        )
        adSlotBuilder.setImageAcceptedSize(
            CsjProvider.Splash.imageAcceptedSizeWidth,
            CsjProvider.Splash.imageAcceptedSizeHeight
        )
        TogetherAdCsj.mTTAdManager.createAdNative(activity)
            .loadSplashAd(adSlotBuilder.build(), object : TTAdNative.CSJSplashAdListener {
                override fun onSplashLoadSuccess() {

                }

                override fun onSplashLoadFail(error: CSJAdError?) {
                    callbackSplashFailed(adProviderType, alias, listener, error?.code, error?.msg)
                }

                override fun onSplashRenderSuccess(splashAd: CSJSplashAd?) {
                    if (splashAd == null) {
                        callbackSplashFailed(
                            adProviderType,
                            alias,
                            listener,
                            null,
                            "请求成功，但是返回的广告为null"
                        )
                        return
                    }
                    callbackSplashLoaded(adProviderType, alias, listener)
                    mSplashAd = splashAd
                    mSplashAd?.setSplashAdListener(object : CSJSplashAd.SplashAdListener {
                        override fun onSplashAdShow(p0: CSJSplashAd?) {
                            callbackSplashExposure(adProviderType, listener)
                        }

                        override fun onSplashAdClick(p0: CSJSplashAd?) {
                            callbackSplashClicked(adProviderType, listener)
                        }

                        override fun onSplashAdClose(p0: CSJSplashAd?, p1: Int) {
                            CsjProvider.Splash.customSkipView = null
                            callbackSplashDismiss(adProviderType, listener)
                        }

                    })
                }

                override fun onSplashRenderFail(p0: CSJSplashAd?, error: CSJAdError?) {
                    callbackSplashFailed(adProviderType, alias, listener, error?.code, error?.msg)
                }
            }, CsjProvider.Splash.maxFetchDelay)
    }

    override fun showSplashAd(container: ViewGroup): Boolean {

        if (mSplashAd?.splashView == null) {
            return false
        }
        container.removeAllViews()
        mSplashAd?.showSplashView(container)
        return true
    }

    override fun loadAndShowSplashAd(
        activity: FragmentActivity,
        adProviderType: String,
        alias: String,
        container: ViewGroup,
        listener: SplashListener
    ) {

        callbackSplashStartRequest(adProviderType, alias, listener)

        val adSlotBuilder = AdSlot.Builder()
        adSlotBuilder.setAdLoadType(TogetherAdCsj.adLoadType)
        adSlotBuilder.setCodeId(TogetherAdCsj.idMapCsj[alias])
        adSlotBuilder.setExpressViewAcceptedSize(
            CsjProvider.Splash.imageAcceptedSizeWidthDp,
            CsjProvider.Splash.imageAcceptedSizeHeightDp
        )
        adSlotBuilder.setImageAcceptedSize(
            CsjProvider.Splash.imageAcceptedSizeWidth,
            CsjProvider.Splash.imageAcceptedSizeHeight
        )
        TogetherAdCsj.mTTAdManager.createAdNative(activity)
            .loadSplashAd(adSlotBuilder.build(), object : TTAdNative.CSJSplashAdListener {
                override fun onSplashLoadSuccess() {

                }

                override fun onSplashLoadFail(error: CSJAdError?) {
                    callbackSplashFailed(adProviderType, alias, listener, error?.code, error?.msg)
                }

                override fun onSplashRenderSuccess(splashAd: CSJSplashAd?) {
                    if (splashAd == null) {
                        callbackSplashFailed(
                            adProviderType,
                            alias,
                            listener,
                            null,
                            "请求成功，但是返回的广告为null"
                        )
                        return
                    }
                    callbackSplashLoaded(adProviderType, alias, listener)
                    mSplashAd = splashAd
                    showSplashAd(container)
                    splashAd.setSplashAdListener(object : CSJSplashAd.SplashAdListener {
                        override fun onSplashAdShow(p0: CSJSplashAd?) {
                            callbackSplashExposure(adProviderType, listener)
                        }

                        override fun onSplashAdClick(p0: CSJSplashAd?) {
                            callbackSplashClicked(adProviderType, listener)
                        }

                        override fun onSplashAdClose(p0: CSJSplashAd?, p1: Int) {
                            CsjProvider.Splash.customSkipView = null
                            callbackSplashDismiss(adProviderType, listener)
                        }
                    })
                }

                override fun onSplashRenderFail(p0: CSJSplashAd?, error: CSJAdError?) {
                    callbackSplashFailed(adProviderType, alias, listener, error?.code, error?.msg)
                }

            }, CsjProvider.Splash.maxFetchDelay)
    }


    override fun destroySplashAd(context: Context) {

    }
}