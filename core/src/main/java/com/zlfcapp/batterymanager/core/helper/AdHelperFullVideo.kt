package com.zlfcapp.batterymanager.core.helper

import android.app.Activity
import androidx.fragment.app.FragmentActivity
import com.zlfcapp.batterymanager.core.R
import com.zlfcapp.batterymanager.core.TogetherAd
import com.zlfcapp.batterymanager.core.config.AdProviderLoader
import com.zlfcapp.batterymanager.core.listener.FullVideoListener
import com.zlfcapp.batterymanager.core.provider.BaseAdProvider
import com.zlfcapp.batterymanager.core.utils.DispatchUtil
import com.zlfcapp.batterymanager.core.utils.loge
import org.jetbrains.annotations.NotNull
import java.lang.ref.WeakReference

/**
 *
 * Created by Matthew Chen on 2020/12/1.
 */
class AdHelperFullVideo(

    @NotNull activity: FragmentActivity,
    @NotNull alias: String,
    ratioMap: LinkedHashMap<String, Int>? = null,
    listener: FullVideoListener? = null

) : BaseHelper() {

    private var mActivity: WeakReference<FragmentActivity> = WeakReference(activity)
    private var mAlias: String = alias
    private var mRatioMap: LinkedHashMap<String, Int>? = ratioMap
    private var mListener: FullVideoListener? = listener
    private var adProvider: BaseAdProvider? = null

    //为了照顾 Java 调用的同学
    constructor(
            @NotNull activity: FragmentActivity,
            @NotNull alias: String,
            listener: FullVideoListener? = null
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

        adProvider?.requestFullVideoAd(mActivity.get()!!, adProviderType, mAlias, object : FullVideoListener {
            override fun onAdStartRequest(providerType: String) {
                mListener?.onAdStartRequest(providerType)
            }

            override fun onAdFailed(providerType: String, failedMsg: String?) {
                if (isFetchOverTime) return

                reload(filterType(ratioMap, adProviderType))

                mListener?.onAdFailed(providerType, failedMsg)
            }

            override fun onAdClicked(providerType: String) {
                mListener?.onAdClicked(providerType)
            }

            override fun onAdShow(providerType: String) {
                mListener?.onAdShow(providerType)
            }

            override fun onAdLoaded(providerType: String) {
                if (isFetchOverTime) return

                cancelTimer()
                mListener?.onAdLoaded(providerType)
            }

            override fun onAdVideoCached(providerType: String) {
                mListener?.onAdVideoCached(providerType)
            }

            override fun onAdVideoComplete(providerType: String) {
                mListener?.onAdVideoComplete(providerType)
            }

            override fun onAdClose(providerType: String) {
                mListener?.onAdClose(providerType)
            }
        })
    }

    fun show() {
        mActivity.get()?.let { adProvider?.showFullVideoAd(it) }
    }
}