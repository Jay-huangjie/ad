package com.ifmvo.togetherad.gdt.provider

import android.app.Activity
import android.os.SystemClock
import android.view.ViewGroup
import com.ifmvo.togetherad.core.listener.SplashListener
import com.ifmvo.togetherad.core.utils.logi
import com.ifmvo.togetherad.core.utils.logv
import com.ifmvo.togetherad.gdt.TogetherAdGdt
import com.qq.e.ads.splash.SplashAD
import com.qq.e.ads.splash.SplashADListener
import com.qq.e.comm.util.AdError
import kotlin.math.roundToInt

/**
 * Created by Matthew Chen on 2020/11/25.
 */
abstract class GdtProviderSplash : GdtProviderReward() {

    private var splashAd: SplashAD? = null
    private var mExpireTimestamp = 0L//广告失效的时间戳
    private var mContainer: ViewGroup? = null

    override fun loadOnlySplashAd(activity: Activity, adProviderType: String, alias: String, listener: SplashListener) {
        callbackSplashStartRequest(adProviderType, alias, listener)

        splashAd = SplashAD(activity, TogetherAdGdt.idMapGDT[alias], object : SplashADListener {

            override fun onADDismissed() {
                mContainer = null
                splashAd = null
                callbackSplashDismiss(adProviderType, listener)
            }

            override fun onNoAD(adError: AdError?) {
                mContainer = null
                splashAd = null
                callbackSplashFailed(adProviderType, alias, listener, adError?.errorCode, adError?.errorMsg)
            }

            /**
             * 广告成功展示时调用，成功展示不等于有效展示（比如广告容器高度不够）
             */
            override fun onADPresent() {
                "${adProviderType}: 广告成功展示".logi(tag)
                splashAd?.preLoad()
            }

            override fun onADClicked() {
                callbackSplashClicked(adProviderType, listener)
            }

            override fun onADTick(millisUntilFinished: Long) {
                val second = (millisUntilFinished / 1000f).roundToInt()
                "${adProviderType}: 倒计时: $second".logv(tag)
            }

            override fun onADExposure() {
                callbackSplashExposure(adProviderType, listener)
            }

            /**
             * 广告加载成功的回调，在fetchAdOnly的情况下，表示广告拉取成功可以显示了。广告需要在SystemClock.elapsedRealtime <expireTimestamp前展示，否则在showAd时会返回广告超时错误。
             */
            override fun onADLoaded(expireTimestamp: Long) {
                mExpireTimestamp = expireTimestamp
                TogetherAdGdt.downloadConfirmListener?.let {
                    splashAd?.setDownloadConfirmListener(it)
                }
                callbackSplashLoaded(adProviderType, alias, listener)
            }
            /**
             * fetchDelay 参数，设置开屏广告从请求到展示所花的最大时长（并不是指广告曝光时长），
             * 取值范围为[3000, 5000]ms。
             * 如果需要使用默认值，可以调用上一个构造方法，或者给 fetchDelay 设为0。
             */
        }, GdtProvider.Splash.maxFetchDelay)

        splashAd?.fetchAdOnly()
    }

    override fun showSplashAd(container: ViewGroup): Boolean {

        if (splashAd == null) {
            return false
        }

        //广告失效了
        if (SystemClock.elapsedRealtime() >= mExpireTimestamp) {
            return false
        }

        mContainer = container
        splashAd?.showAd(container)

        return true
    }

    override fun loadAndShowSplashAd(activity: Activity, adProviderType: String, alias: String, container: ViewGroup, listener: SplashListener) {

        callbackSplashStartRequest(adProviderType, alias, listener)

        splashAd = SplashAD(activity, TogetherAdGdt.idMapGDT[alias], object : SplashADListener {

            override fun onADDismissed() {
                splashAd = null
                callbackSplashDismiss(adProviderType, listener)
            }

            override fun onNoAD(adError: AdError?) {
                splashAd = null
                callbackSplashFailed(adProviderType, alias, listener, adError?.errorCode, adError?.errorMsg)
            }

            /**
             * 广告成功展示时调用，成功展示不等于有效展示（比如广告容器高度不够）
             */
            override fun onADPresent() {
                "${adProviderType}: 广告成功展示".logi(tag)
            }

            override fun onADClicked() {
                callbackSplashClicked(adProviderType, listener)
            }

            override fun onADTick(millisUntilFinished: Long) {
                val second = (millisUntilFinished / 1000f).roundToInt()
                "${adProviderType}: 倒计时: $second".logv(tag)
            }

            override fun onADExposure() {
                callbackSplashExposure(adProviderType, listener)
            }

            /**
             * 广告加载成功的回调，在fetchAdOnly的情况下，表示广告拉取成功可以显示了。广告需要在SystemClock.elapsedRealtime <expireTimestamp前展示，否则在showAd时会返回广告超时错误。
             */
            override fun onADLoaded(expireTimestamp: Long) {
                TogetherAdGdt.downloadConfirmListener?.let {
                    splashAd?.setDownloadConfirmListener(it)
                }
                callbackSplashLoaded(adProviderType, alias, listener)
            }
        }, GdtProvider.Splash.maxFetchDelay)
        /**
         * fetchDelay 参数，设置开屏广告从请求到展示所花的最大时长（并不是指广告曝光时长），
         * 取值范围为[3000, 5000]ms。
         * 如果需要使用默认值，可以调用上一个构造方法，或者给 fetchDelay 设为0。
         */
        splashAd?.fetchAndShowIn(container)
    }

}