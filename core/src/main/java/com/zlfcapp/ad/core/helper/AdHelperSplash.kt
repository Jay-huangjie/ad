package com.zlfcapp.ad.core.helper

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.zlfcapp.ad.core.R
import com.zlfcapp.ad.core.TogetherAd
import com.zlfcapp.ad.core.config.AdProviderLoader
import com.zlfcapp.ad.core.listener.SplashListener
import com.zlfcapp.ad.core.utils.DispatchUtil
import com.zlfcapp.ad.core.utils.loge
import org.jetbrains.annotations.NotNull

/**
 * 开屏广告
 *
 * Created by Matthew Chen on 2020-04-03.
 */
object AdHelperSplash : BaseHelper() {

    //为了照顾 Java 调用的同学
    fun show(@NotNull activity: FragmentActivity, @NotNull alias: String, @NotNull container: ViewGroup, listener: SplashListener? = null) {
        show(activity, alias, null, container, listener)
    }

    fun show(@NotNull activity: FragmentActivity, @NotNull alias: String, ratioMap: LinkedHashMap<String, Int>? = null, @NotNull container: ViewGroup, listener: SplashListener? = null) {
        startTimer(listener)
        realShow(activity, alias, ratioMap, container, listener)
    }

    private fun realShow(@NotNull activity: FragmentActivity, @NotNull alias: String, ratioMap: LinkedHashMap<String, Int>? = null, @NotNull container: ViewGroup, listener: SplashListener? = null) {
        val currentRatioMap = if (ratioMap?.isEmpty() != false) TogetherAd.getPublicProviderRatio() else ratioMap

        val adProviderType = DispatchUtil.getAdProvider(alias, currentRatioMap)

        if (adProviderType?.isEmpty() != false) {
            cancelTimer()
            listener?.onAdFailedAll(FailedAllMsg.failedAll_noDispatch)
            return
        }

        val adProvider = AdProviderLoader.loadAdProvider(adProviderType)

        if (adProvider == null) {
            "$adProviderType ${activity.getString(R.string.no_init)}".loge()
            val newRatioMap = filterType(currentRatioMap, adProviderType)
            realShow(activity, alias, newRatioMap, container, listener)
            return
        }

        adProvider.loadAndShowSplashAd(activity = activity, adProviderType = adProviderType, alias = alias, container = container, listener = object : SplashListener {
            override fun onAdFailed(providerType: String, failedMsg: String?) {
                if (isFetchOverTime) return

                val newRatioMap = filterType(currentRatioMap, adProviderType)
                realShow(activity, alias, newRatioMap, container, listener)

                listener?.onAdFailed(providerType, failedMsg)
            }

            override fun onAdStartRequest(providerType: String) {
                listener?.onAdStartRequest(providerType)
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
}