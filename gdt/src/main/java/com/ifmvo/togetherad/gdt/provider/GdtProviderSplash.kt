package com.ifmvo.togetherad.gdt.provider

import android.R
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.ifmvo.togetherad.core.listener.EyesSplashListener
import com.ifmvo.togetherad.core.listener.SplashListener
import com.ifmvo.togetherad.core.utils.logi
import com.ifmvo.togetherad.core.utils.logv
import com.ifmvo.togetherad.gdt.TogetherAdGdt
import com.ifmvo.togetherad.gdt.utils.SplashZoomOutManager
import com.qq.e.ads.splash.SplashAD
import com.qq.e.ads.splash.SplashADListener
import com.qq.e.ads.splash.SplashADZoomOutListener
import com.qq.e.comm.util.AdError
import java.lang.Exception
import kotlin.math.roundToInt

/**
 * Created by Matthew Chen on 2020/11/25.
 */
abstract class GdtProviderSplash : GdtProviderReward() {

    private var splashAd: SplashAD? = null
    private var mExpireTimestamp = 0L//广告失效的时间戳
    private var mContainer: ViewGroup? = null

    override fun loadOnlySplashAd(
        activity: Activity,
        adProviderType: String,
        alias: String,
        listener: SplashListener
    ) {
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
                callbackSplashFailed(
                    adProviderType,
                    alias,
                    listener,
                    adError?.errorCode,
                    adError?.errorMsg
                )
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

    override fun loadAndShowSplashAd(
        activity: Activity,
        adProviderType: String,
        alias: String,
        container: ViewGroup,
        listener: SplashListener
    ) {

        callbackSplashStartRequest(adProviderType, alias, listener)

        splashAd = SplashAD(activity, TogetherAdGdt.idMapGDT[alias], object : SplashADListener {

            override fun onADDismissed() {
                splashAd = null
                callbackSplashDismiss(adProviderType, listener)
            }

            override fun onNoAD(adError: AdError?) {
                splashAd = null
                callbackSplashFailed(
                    adProviderType,
                    alias,
                    listener,
                    adError?.errorCode,
                    adError?.errorMsg
                )
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


    override fun loadSplashEye(
        activity: Activity,
        adProviderType: String,
        alias: String,
        container: ViewGroup,
        listener: EyesSplashListener
    ) {
        callbackSplashStartRequest(adProviderType, alias, listener)
        splashAd =
            SplashAD(activity, TogetherAdGdt.idMapGDT[alias], object : SplashADZoomOutListener {

                private var isZoomOut = false

                override fun onADDismissed() {
                    splashAd = null
                    callbackSplashDismiss(adProviderType, listener)
                    Log.e("HJ", "onADDismissed")

//                    if (zoomOutView != null) {
//                        ViewUtils.removeFromParent(zoomOutView)
//                    }
                }

                override fun onNoAD(adError: AdError?) {
                    splashAd = null
                    Log.e("HJ", adError?.errorMsg + "----")
                    callbackSplashFailed(
                        adProviderType,
                        alias,
                        listener,
                        adError?.errorCode,
                        adError?.errorMsg
                    )
                }

                /**
                 * 广告成功展示时调用，成功展示不等于有效展示（比如广告容器高度不够）
                 */
                override fun onADPresent() {
                    "${adProviderType}: 广告成功展示".logi(tag)
                }

                override fun onADClicked() {
                    Log.e("HJ", "onADClicked")
                    callbackSplashClicked(adProviderType, listener)
                }

                override fun onADTick(millisUntilFinished: Long) {
                    val second = (millisUntilFinished / 1000f).roundToInt()
                    "${adProviderType}: 倒计时: $second".logv(tag)
                    Log.e("HJ", "onADTick")
                }

                override fun onADExposure() {
                    callbackSplashExposure(adProviderType, listener)
                }

                /**
                 * 广告加载成功的回调，在fetchAdOnly的情况下，表示广告拉取成功可以显示了。广告需要在SystemClock.elapsedRealtime <expireTimestamp前展示，否则在showAd时会返回广告超时错误。
                 */
                override fun onADLoaded(expireTimestamp: Long) {
                    Log.e("HJ", "onADLoaded")
                    TogetherAdGdt.downloadConfirmListener?.let {
                        splashAd?.setDownloadConfirmListener(it)
                    }
                    callbackSplashLoaded(adProviderType, alias, listener)
                }

                override fun onZoomOut() {
                    Log.e("HJ", "onZoomOut")
                    isZoomOut = true
//                    //防止移除view后显示底图导致屏幕闪烁
//                    val b: Bitmap? = splashAd?.zoomOutBitmap
//                    if (b != null) {
//                        container.setScaleType(ImageView.ScaleType.CENTER_CROP)
//                        container.setImageBitmap(b)
//                    }
                    val zoomOutManager: SplashZoomOutManager = SplashZoomOutManager.getInstance()
                    zoomOutManager.setSplashInfo(
                        splashAd, container.getChildAt(0),
                        activity.getWindow().getDecorView()
                    )
                }

                override fun onZoomOutPlayFinish() {
                    Log.e("HJ", "onZoomOutPlayFinish")
                }

                override fun isSupportZoomOut(): Boolean {
                    return true
                }
            }, GdtProvider.Splash.maxFetchDelay)
        splashAd?.fetchAndShowIn(container)
    }

    override fun loadMainSplashEye(
        activity: Activity,
        listener: EyesSplashListener
    ) {
        val addZoomOut = addZoomOut(activity)
        if (addZoomOut != null) {
            activity.overridePendingTransition(0, 0)
        }
    }

    open fun addZoomOut(activity: Activity): View? {
        val zoomOutManager = SplashZoomOutManager.getInstance()
        val zoomAd = zoomOutManager.splashAD
        return zoomOutManager.startZoomOut(activity.window.decorView as ViewGroup,
            activity.findViewById(R.id.content), object : SplashZoomOutManager.AnimationCallBack {
                override fun animationStart(animationTime: Int) {
                }

                override fun animationEnd() {
                    zoomAd.zoomOutAnimationFinish()
                }
            })
    }

}