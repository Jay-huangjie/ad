package com.zlfcapp.ad.core.helper

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.zlfcapp.ad.core.R
import com.zlfcapp.ad.core.TogetherAd
import com.zlfcapp.ad.core.config.AdProviderLoader
import com.zlfcapp.ad.core.listener.SplashListener
import com.zlfcapp.ad.core.provider.BaseAdProvider
import com.zlfcapp.ad.core.utils.DispatchUtil
import com.zlfcapp.ad.core.utils.loge
import org.jetbrains.annotations.NotNull
import java.lang.ref.WeakReference

/**
 * 开屏广告
 *
 * Created by Matthew Chen on 2020-04-03.
 */
class AdHelperSplashPro(

    @NotNull activity: FragmentActivity,
    @NotNull alias: String,
    ratioMap: LinkedHashMap<String, Int>? = null

) : BaseHelper() {

    private var mActivity: WeakReference<FragmentActivity> = WeakReference(activity)
    private var mAlias: String = alias
    private var mRatioMap: LinkedHashMap<String, Int>? = ratioMap
    private var adProvider: BaseAdProvider? = null

    //为了照顾 Java 调用的同学
    constructor(
            @NotNull activity: FragmentActivity,
            @NotNull alias: String
    ) : this(activity, alias, null)

    //为了照顾 Java 调用的同学
    fun loadOnly(listener: SplashListener? = null) {
        val currentRatioMap = if (mRatioMap?.isEmpty() != false) TogetherAd.getPublicProviderRatio() else mRatioMap!!

        startTimer(listener)
        realLoadOnly(currentRatioMap, listener)
    }

    private fun realLoadOnly(@NotNull ratioMap: LinkedHashMap<String, Int>, listener: SplashListener? = null) {

        val adProviderType = DispatchUtil.getAdProvider(mAlias, ratioMap)

        if (adProviderType?.isEmpty() != false || mActivity.get() == null) {
            cancelTimer()
            listener?.onAdFailedAll(FailedAllMsg.failedAll_noDispatch)
            return
        }

        adProvider = AdProviderLoader.loadAdProvider(adProviderType)

        if (adProvider == null) {
            "$adProviderType ${mActivity.get()?.getString(R.string.no_init)}".loge()
            realLoadOnly(filterType(ratioMap, adProviderType))
            return
        }

        adProvider?.loadOnlySplashAd(activity = mActivity.get()!!, adProviderType = adProviderType, alias = mAlias, listener = object : SplashListener {
            override fun onAdFailed(providerType: String, failedMsg: String?) {
                if (isFetchOverTime) return

                realLoadOnly(filterType(ratioMap, adProviderType), listener)
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

    fun showAd(@NotNull container: ViewGroup): Boolean {
        return adProvider?.showSplashAd(container) ?: false
    }
}