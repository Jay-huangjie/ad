package com.ifmvo.togetherad.demo.extend

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import com.ifmvo.togetherad.core.listener.BannerListener
import com.ifmvo.togetherad.core.utils.loge
import com.ifmvo.togetherad.csj.TogetherAdCsj
import com.ifmvo.togetherad.csj.provider.CsjProvider

/**
 *
 * Created by Matthew Chen on 2020/10/23.
 */
class CustomCsjProvider : CsjProvider() {

    object Banner {

        var supportDeepLink: Boolean = true

        //图片的宽高
        internal var imageAcceptedSizeWidth = 600

        internal var imageAcceptedSizeHeight = 257

        fun setImageAcceptedSize(width: Int, height: Int) {
            imageAcceptedSizeWidth = width
            imageAcceptedSizeHeight = height
        }

        //Banner 刷新间隔时间
        var slideIntervalTime = 30 * 1000
    }

    /**
     * 这里只重写相应的方法即可
     */
    override fun showBannerAd(activity: Activity, adProviderType: String, alias: String, container: ViewGroup, listener: BannerListener) {
        callbackBannerStartRequest(adProviderType, alias, listener)
        destroyBannerAd()
    }

}