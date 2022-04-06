package com.ifmvo.togetherad.core.helper

import android.app.Activity
import com.ifmvo.togetherad.core.R
import org.jetbrains.annotations.NotNull
import com.ifmvo.togetherad.core.TogetherAd
import com.ifmvo.togetherad.core.config.AdProviderLoader
import com.ifmvo.togetherad.core.listener.InterListener
import com.ifmvo.togetherad.core.provider.BaseAdProvider
import com.ifmvo.togetherad.core.utils.DispatchUtil
import com.ifmvo.togetherad.core.utils.loge
import java.lang.ref.WeakReference

/**
 * 激励广告
 *
 * Created by Matthew Chen on 2020-04-20.
 */
class AdHelperInter(

        @NotNull activity: Activity,
        @NotNull alias: String,
        ratioMap: LinkedHashMap<String, Int>? = null,
        listener: InterListener? = null

) : BaseHelper() {

    private var mActivity: WeakReference<Activity> = WeakReference(activity)
    private var mAlias: String = alias
    private var mRatioMap: LinkedHashMap<String, Int>? = ratioMap
    private var mListener: InterListener? = listener
    private var adProvider: BaseAdProvider? = null

    //为了照顾 Java 调用的同学
    constructor(
            @NotNull activity: Activity,
            @NotNull alias: String,
            listener: InterListener? = null
    ) : this(activity, alias, null, listener)

    fun load() {
        val currentRatioMap: LinkedHashMap<String, Int> = if (mRatioMap?.isEmpty() != false) TogetherAd.getPublicProviderRatio() else mRatioMap!!

        startTimer(mListener)
        reload(currentRatioMap)
    }

    private fun reload(@NotNull ratioMap: LinkedHashMap<String, Int>) {

        val adProviderType = DispatchUtil.getAdProvider(mAlias, ratioMap)

        if (adProviderType?.isEmpty() != false || mActivity.get() == null) {
            cancelTimer()
            mListener?.onAdFailedAll(FailedAllMsg.failedAll_noDispatch)
            return
        }

        adProvider = AdProviderLoader.loadAdProvider(adProviderType)

        if (adProvider == null) {
            "$adProviderType ${mActivity.get()?.getString(R.string.no_init)}".loge()
            reload(filterType(ratioMap, adProviderType))
            return
        }

        adProvider?.requestInterAd(mActivity.get()!!, adProviderType, mAlias, object : InterListener {
            override fun onAdStartRequest(providerType: String) {
                mListener?.onAdStartRequest(providerType)
            }

            override fun onAdLoaded(providerType: String) {
                if (isFetchOverTime) return

                cancelTimer()
                mListener?.onAdLoaded(providerType)
            }

            override fun onAdFailed(providerType: String, failedMsg: String?) {
                if (isFetchOverTime) return

                reload(filterType(ratioMap, adProviderType))

                mListener?.onAdFailed(providerType, failedMsg)
            }

            override fun onAdClicked(providerType: String) {
                mListener?.onAdClicked(providerType)
            }

            override fun onAdExpose(providerType: String) {
                mListener?.onAdExpose(providerType)
            }

            override fun onAdClose(providerType: String) {
                mListener?.onAdClose(providerType)
            }
        })
    }

    fun show() {
        mActivity.get()?.let { adProvider?.showInterAd(it) }
    }

    fun destroy() {
        adProvider?.destroyInterAd()
        adProvider = null
    }
}