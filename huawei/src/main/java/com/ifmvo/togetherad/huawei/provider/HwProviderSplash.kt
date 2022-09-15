package com.ifmvo.togetherad.huawei.provider

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.huawei.appgallery.agd.agdpro.api.*
import com.zlfcapp.ad.huawei.R
import com.ifmvo.togetherad.huawei.TogetherAdHw
import com.huawei.appgallery.agd.core.api.AdSlot
import com.huawei.appgallery.agd.core.api.AgdAdConstant
import com.ifmvo.togetherad.core.listener.SplashListener
import com.ifmvo.togetherad.core.provider.BaseAdProvider
import org.json.JSONObject

abstract class HwProviderSplash : BaseAdProvider(), LifecycleEventObserver {

    private var adList: MutableList<ITemplateAd>? = null

    override fun loadAndShowSplashAd(
        activity: FragmentActivity,
        adProviderType: String,
        alias: String,
        container: ViewGroup,
        listener: SplashListener
    ) {
        callbackSplashStartRequest(adProviderType, alias, listener)
        val adsContext = AdsContext(activity)
        val splashSetting = HuaweiProvider.Splash
        val build = AdSlot.Builder()
            .slotId(TogetherAdHw.idMapKs[alias]) // 广告槽位ID
            .darkMode(HuaweiProvider.ADSlot.darkMode) // 深色模式开关
            .disableSdkCountDown(HuaweiProvider.Splash.disableSdkCountDown)
        if (HuaweiProvider.Splash.width != -1 && HuaweiProvider.Splash.height != -1) {
            build.acceptedSize(HuaweiProvider.Splash.width, HuaweiProvider.Splash.height)
        }
        build.mediaExtra(getJsonMediaExtra())
        val adSlot = build.build()

        adsContext.loadSplashAds(adSlot, object : TemplateLoadListener {
            override fun onError(code: Int, message: String?) {
                callbackSplashFailed(adProviderType, alias, listener, code, message)
            }

            override fun onAdLoad(ads: MutableList<ITemplateAd>?) {
                if (ads != null && ads.isNotEmpty()) {
                    adList = ads
                    val ad = ads[0]
                    callbackSplashLoaded(adProviderType, alias, listener)
                    ad.setInteractionListener(object : SplashInteractionListener {
                        override fun onAdClicked(p0: View?) {
                            callbackSplashClicked(adProviderType, listener)
                        }

                        override fun onAdShow(p0: View?) {
                            callbackSplashExposure(adProviderType, listener)
                        }

                        override fun onRenderFail(view: View?, code: Int, message: String?) {
                            ad.destroy()
                            ads.removeAt(0)
                            callbackSplashFailed(adProviderType, alias, listener, code, message)
                        }

                        override fun onRenderSuccess(view: View?, width: Float, height: Float) {
                            view?.let {
                                container.addView(it)
                            }
                        }

                        override fun onAdSkip() {
                            ad.destroy()
                            callbackSplashDismiss(adProviderType, listener)
                        }

                        override fun onAdTimeOver() {
                            callbackSplashDismiss(adProviderType, listener)
                        }
                    })
                    ad.setDislikeClickListener {
                        callbackSplashDismiss(adProviderType, listener)
                    }
                    ad.render()
                }
            }
        })
        activity.lifecycle.addObserver(this)
    }

    fun getJsonMediaExtra(): JSONObject {
        val obj = JSONObject()
        obj.put("referrer", "referrer")
        val obj2 = JSONObject()
        obj2.put("personalize", TogetherAdHw.personalize)
        obj.put("personalize", obj2)
        return obj
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_DESTROY -> {
                if (adList != null && adList!!.isNotEmpty()) {
                    if (adList?.get(0) != null) {
                        adList!![0].destroy()
                        adList!!.removeAt(0)
                    }
                }
            }
            else -> {}
        }
    }

    override fun loadOnlySplashAd(
        activity: FragmentActivity,
        adProviderType: String,
        alias: String,
        listener: SplashListener
    ) {
        callbackSplashStartRequest(adProviderType, alias, listener)
        callbackSplashFailed(
            adProviderType,
            alias,
            listener,
            null,
            activity.getString(R.string.hw_can_not)
        )
    }

    fun getShowError(code: Int): String = when (code) {
        AgdAdConstant.REASON_LAYOUT_ERROR -> {
            "动态布局错误"
        }
        AgdAdConstant.REASON_VIDEO_ERROR -> {
            "视频播放异常"
        }
        AgdAdConstant.REASON_LAYOUT_INVALID_PARAM -> {
            "初始化失败"
        }
        else -> ""
    }

    override fun showSplashAd(container: ViewGroup): Boolean {
        return false
    }

}