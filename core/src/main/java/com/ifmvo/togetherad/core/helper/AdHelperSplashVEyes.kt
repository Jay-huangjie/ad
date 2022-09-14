package com.ifmvo.togetherad.core.helper

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.ifmvo.togetherad.core.config.AdProviderLoader
import com.ifmvo.togetherad.core.listener.EyesSplashListener
import com.ifmvo.togetherad.core.provider.BaseAdProvider
import com.ifmvo.togetherad.core.utils.DispatchUtil
import com.ifmvo.togetherad.core.utils.loge
import com.zlfcapp.ad.core.R
import org.jetbrains.annotations.NotNull

/**
 *  create by hj on 2022/4/6
 **/
object AdHelperSplashVEyes : BaseHelper() {

    var adProvider: BaseAdProvider? = null


    //为了照顾 Java 调用的同学
    fun show(
        @NotNull activity: FragmentActivity,
        @NotNull alias: String,
        @NotNull container: ViewGroup,
        listener: EyesSplashListener? = null
    ) {
        show(activity, alias, null, container, listener)
    }

    fun show(
        @NotNull activity: FragmentActivity,
        @NotNull alias: String,
        ratioMap: LinkedHashMap<String, Int>? = null,
        @NotNull container: ViewGroup,
        listener: EyesSplashListener? = null
    ) {
        startTimer(listener)
        realShow(activity, alias, ratioMap, container, listener)
    }

    private fun realShow(
        @NotNull activity: FragmentActivity,
        @NotNull alias: String,
        ratioMap: LinkedHashMap<String, Int>? = null,
        @NotNull container: ViewGroup,
        listener: EyesSplashListener? = null
    ) {
        val currentRatioMap =
            if (ratioMap?.isEmpty() != false) com.ifmvo.togetherad.core.TogetherAd.getPublicProviderRatio() else ratioMap

        val adProviderType = DispatchUtil.getAdProvider(alias, currentRatioMap)

        if (adProviderType?.isEmpty() != false) {
            cancelTimer()
            listener?.onAdFailedAll(FailedAllMsg.failedAll_noDispatch)
            return
        }

        adProvider = AdProviderLoader.loadAdProvider(adProviderType)

        if (adProvider == null) {
            "$adProviderType ${activity.getString(R.string.no_init)}".loge()
            val newRatioMap = filterType(currentRatioMap, adProviderType)
            realShow(activity, alias, newRatioMap, container, listener)
            return
        }

        adProvider?.loadSplashEye(
            activity = activity,
            adProviderType = adProviderType,
            alias = alias,
            container = container,
            listener = object : EyesSplashListener {
                override fun onAdFailed(providerType: String, failedMsg: String?) {
                    if (isFetchOverTime) return

                    val newRatioMap = filterType(currentRatioMap, adProviderType)
                    realShow(activity, alias, newRatioMap, container, listener)

                    listener?.onAdFailed(providerType, failedMsg)
                }

                override fun onAdStartRequest(providerType: String) {
                    listener?.onAdStartRequest(providerType)
                }

                override fun eyesClose() {
                    listener?.eyesClose()
                }

                override fun onAdLoaded(providerType: String) {
                    if (isFetchOverTime) return

                    cancelTimer()
                    listener?.onAdLoaded(providerType)
                }

                override fun onAdClicked(providerType: String) {
                    listener?.onAdClicked(providerType)
                }

                override fun onAdExposure(providerType: String) {
                    listener?.onAdExposure(providerType)
                }

                override fun onAdDismissed(providerType: String) {
                    listener?.onAdDismissed(providerType)
                }
            })
    }

    fun initSplashClickEyeData(
        @NotNull activity: FragmentActivity,
        listener: EyesSplashListener? = null
    ) {
        if (adProvider == null) {
            "$adProvider 闪屏页adProvider获取失败".loge()
            return
        }
        adProvider?.loadMainSplashEye(activity,object :EyesSplashListener{

            override fun eyesClose() {
                listener?.eyesClose()
            }

            override fun onAdLoaded(providerType: String) {
                listener?.onAdLoaded(providerType)
            }

            override fun onAdClicked(providerType: String) {
                listener?.onAdClicked(providerType)
            }

            override fun onAdExposure(providerType: String) {
                listener?.onAdExposure(providerType)
            }

            override fun onAdDismissed(providerType: String) {
                listener?.onAdDismissed(providerType)
            }
        })
    }
}