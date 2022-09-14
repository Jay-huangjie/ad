package com.zlfcapp.batterymanager.core.helper

import android.app.Activity
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.zlfcapp.batterymanager.core.R
import com.zlfcapp.batterymanager.core.TogetherAd
import com.zlfcapp.batterymanager.core.config.AdProviderLoader
import com.zlfcapp.batterymanager.core.custom.native_.BaseNativeTemplate
import com.zlfcapp.batterymanager.core.listener.NativeListener
import com.zlfcapp.batterymanager.core.listener.NativeViewListener
import com.zlfcapp.batterymanager.core.provider.BaseAdProvider
import com.zlfcapp.batterymanager.core.utils.DispatchUtil
import com.zlfcapp.batterymanager.core.utils.loge
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable
import java.lang.ref.WeakReference

/**
 * 原生信息流广告
 *
 * Created by Matthew Chen on 2020-04-20.
 */
class AdHelperNativePro(
    @NotNull activity: FragmentActivity,
    @NotNull alias: String,
    ratioMap: LinkedHashMap<String, Int>? = null,
    maxCount: Int

) : BaseHelper() {

    private var mActivity: WeakReference<FragmentActivity> = WeakReference(activity)
    private var mAlias: String = alias
    private var mRatioMap: LinkedHashMap<String, Int>? = ratioMap
    private var mMaxCount: Int = maxCount
    private var adProvider: BaseAdProvider? = null

    //所有请求到的广告容器
    private var mAdList = mutableListOf<Any>()

    companion object {

        private const val defaultMaxCount = 1

        fun show(@Nullable adObject: Any?, @Nullable container: ViewGroup?, @NotNull nativeTemplate: BaseNativeTemplate, @Nullable listener: NativeViewListener? = null) {
            if (adObject == null) {
                return
            }
            if (container == null) {
                return
            }
            TogetherAd.mProviders.entries.forEach { entry ->
                val adProvider = AdProviderLoader.loadAdProvider(entry.key)
                if (adProvider?.nativeAdIsBelongTheProvider(adObject) == true) {
                    val nativeView = nativeTemplate.getNativeView(entry.key)
                    nativeView?.showNative(entry.key, adObject, container, listener)
                    return@forEach
                }
            }
        }

        fun pauseAd(@Nullable adObject: Any?) {
            if (adObject == null) {
                return
            }
            TogetherAd.mProviders.entries.forEach { entry ->
                val adProvider = AdProviderLoader.loadAdProvider(entry.key)
                adProvider?.pauseNativeAd(adObject)
            }
        }

        fun pauseAd(@Nullable adObjectList: List<Any>?) {
            if (adObjectList?.isEmpty() != false) {
                return
            }
            adObjectList.forEach { pauseAd(it) }
        }

        fun resumeAd(@Nullable adObject: Any?) {
            if (adObject == null) {
                return
            }
            TogetherAd.mProviders.entries.forEach { entry ->
                val adProvider = AdProviderLoader.loadAdProvider(entry.key)
                adProvider?.resumeNativeAd(adObject)
            }
        }

        fun resumeAd(@Nullable adObjectList: List<Any>?) {
            if (adObjectList?.isEmpty() != false) {
                return
            }
            adObjectList.forEach { resumeAd(it) }
        }

        fun destroyAd(@Nullable adObject: Any?) {
            if (adObject == null) {
                return
            }
            TogetherAd.mProviders.entries.forEach { entry ->
                val adProvider = AdProviderLoader.loadAdProvider(entry.key)
                adProvider?.destroyNativeAd(adObject)
            }
        }

        fun destroyAd(@Nullable adObjectList: List<Any>?) {
            if (adObjectList?.isEmpty() != false) {
                return
            }
            adObjectList.forEach { destroyAd(it) }
        }
    }

    //为了照顾 Java 调用的同学
    constructor(
            @NotNull activity: FragmentActivity,
            @NotNull alias: String,
            maxCount: Int
    ) : this(activity, alias, null, maxCount)

    //为了照顾 Java 调用的同学
    constructor(
            @NotNull activity: FragmentActivity,
            @NotNull alias: String
    ) : this(activity, alias, null, defaultMaxCount)

    fun getList(listener: NativeListener? = null) {
        val currentRatioMap: LinkedHashMap<String, Int> = if (mRatioMap?.isEmpty() != false) TogetherAd.getPublicProviderRatio() else mRatioMap!!

        startTimer(listener)
        getListForMap(currentRatioMap, listener)
    }

    private fun getListForMap(@NotNull ratioMap: LinkedHashMap<String, Int>, listener: NativeListener? = null) {

        val currentMaxCount = if (mMaxCount <= 0) defaultMaxCount else mMaxCount

        val adProviderType = DispatchUtil.getAdProvider(mAlias, ratioMap)

        if (adProviderType?.isEmpty() != false || mActivity.get() == null) {
            cancelTimer()
            listener?.onAdFailedAll(FailedAllMsg.failedAll_noDispatch)
            return
        }

        adProvider = AdProviderLoader.loadAdProvider(adProviderType)

        if (adProvider == null) {
            "$adProviderType ${mActivity.get()?.getString(R.string.no_init)}".loge()
            getListForMap(filterType(ratioMap, adProviderType), listener)
            return
        }

        adProvider?.getNativeAdList(mActivity.get()!!, adProviderType, mAlias, currentMaxCount, object : NativeListener {
            override fun onAdStartRequest(providerType: String) {
                listener?.onAdStartRequest(providerType)
            }

            override fun onAdLoaded(providerType: String, adList: List<Any>) {
                if (isFetchOverTime) return

                cancelTimer()
                mAdList.addAll(adList)
                listener?.onAdLoaded(providerType, adList)
            }

            override fun onAdFailed(providerType: String, failedMsg: String?) {
                if (isFetchOverTime) return

                getListForMap(filterType(ratioMap, adProviderType), listener)

                listener?.onAdFailed(providerType, failedMsg)
            }
        })
    }

    /**
     * 恢复所有可见的广告
     */
    fun resumeAllAd() {
        resumeAd(mAdList)
    }

    /**
     * 暂停所有可见的广告
     */
    fun pauseAllAd() {
        pauseAd(mAdList)
    }

    /**
     * 销毁所有请求到的广告
     */
    fun destroyAllAd() {
        destroyAd(mAdList)
        mAdList.clear()
    }
}