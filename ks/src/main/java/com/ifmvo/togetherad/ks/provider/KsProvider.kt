package com.ifmvo.togetherad.ks.provider

import android.app.Activity
import android.view.ViewGroup
import com.ifmvo.togetherad.core.listener.EyesSplashListener

class KsProvider : KsProviderSplash() {

    object Reward {
        var isShowLandscape = false
    }


    object FullVideo {
        var isShowLandscape = false
    }

    override fun loadSplashEye(
        activity: Activity,
        adProviderType: String,
        alias: String,
        container: ViewGroup,
        listener: EyesSplashListener
    ) {
    }

    override fun loadMainSplashEye(activity: Activity, listener: EyesSplashListener) {
    }

}