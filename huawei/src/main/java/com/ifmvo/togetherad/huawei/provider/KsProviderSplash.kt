package com.ifmvo.togetherad.huawei.provider

import android.app.Activity
import android.view.ViewGroup
import com.ifmvo.togetherad.core.listener.SplashListener
import com.ifmvo.togetherad.core.provider.BaseAdProvider
import com.ifmvo.togetherad.huawei.R
import com.ifmvo.togetherad.huawei.TogetherAdHw

abstract class KsProviderSplash : BaseAdProvider() {

    override fun loadAndShowSplashAd(activity: Activity, adProviderType: String, alias: String, container: ViewGroup, listener: SplashListener) {
        callbackSplashStartRequest(adProviderType, alias, listener)
    }

    override fun loadOnlySplashAd(activity: Activity, adProviderType: String, alias: String, listener: SplashListener) {
        callbackSplashStartRequest(adProviderType, alias, listener)
        callbackSplashFailed(adProviderType, alias, listener, null, activity.getString(R.string.ks_can_not))
    }

    override fun showSplashAd(container: ViewGroup): Boolean {
        return false
    }

}