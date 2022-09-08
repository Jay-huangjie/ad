package com.ifmvo.togetherad.huawei.provider

import android.app.Activity
import android.view.ViewGroup
import com.ifmvo.togetherad.core.listener.SplashListener
import com.ifmvo.togetherad.core.provider.BaseAdProvider
import com.ifmvo.togetherad.huawei.R
import com.ifmvo.togetherad.huawei.TogetherAdHw
import com.kwad.sdk.api.KsLoadManager
import com.kwad.sdk.api.KsScene
import com.kwad.sdk.api.KsSplashScreenAd

abstract class KsProviderSplash : BaseAdProvider() {

    override fun loadAndShowSplashAd(activity: Activity, adProviderType: String, alias: String, container: ViewGroup, listener: SplashListener) {

        callbackSplashStartRequest(adProviderType, alias, listener)

        if (TogetherAdHw.adRequestManager == null) {
            callbackSplashFailed(adProviderType, alias, listener, null, activity.getString(R.string.ks_init_failed))
            return
        }

        val ksScene = KsScene.Builder(TogetherAdHw.idMapKs[alias] ?: 0)
                .adNum(1)
                .build()

        TogetherAdHw.adRequestManager!!.loadSplashScreenAd(ksScene, object : KsLoadManager.SplashScreenAdListener {

            override fun onRequestResult(adNumber: Int) {}

            override fun onSplashScreenAdLoad(adObject: KsSplashScreenAd?) {
                if (adObject == null) {
                    callbackSplashFailed(adProviderType, alias, listener, null, activity.getString(R.string.ks_ad_null))
                    return
                }

                callbackSplashLoaded(adProviderType, alias, listener)

                val adView = adObject.getView(activity, object : KsSplashScreenAd.SplashScreenAdInteractionListener {
                    override fun onAdShowError(errorCode: Int, errorMsg: String?) {
                        callbackSplashFailed(adProviderType, alias, listener, errorCode, errorMsg)
                    }

                    override fun onAdShowStart() {
                        callbackSplashExposure(adProviderType, listener)
                    }

                    override fun onDownloadTipsDialogShow() {
                        
                    }

                    override fun onAdClicked() {
                        callbackSplashClicked(adProviderType, listener)
                    }

                    override fun onDownloadTipsDialogDismiss() {
                        
                    }

                    override fun onAdShowEnd() {
                        callbackSplashDismiss(adProviderType, listener)
                    }

                    override fun onDownloadTipsDialogCancel() {
                        
                    }

                    override fun onSkippedAd() {
                        callbackSplashDismiss(adProviderType, listener)
                    }
                })

                container.removeAllViews()
                container.addView(adView)
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                callbackSplashFailed(adProviderType, alias, listener, errorCode, errorMsg)
            }
        })
    }

    override fun loadOnlySplashAd(activity: Activity, adProviderType: String, alias: String, listener: SplashListener) {
        callbackSplashStartRequest(adProviderType, alias, listener)
        callbackSplashFailed(adProviderType, alias, listener, null, activity.getString(R.string.ks_can_not))
    }

    override fun showSplashAd(container: ViewGroup): Boolean {
        return false
    }

}