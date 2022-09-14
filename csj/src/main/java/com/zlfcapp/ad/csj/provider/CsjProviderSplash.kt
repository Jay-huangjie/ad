package com.zlfcapp.ad.csj.provider

import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.bytedance.sdk.openadsdk.*
import com.zlfcapp.ad.core.listener.EyesSplashListener
import com.zlfcapp.ad.core.listener.SplashListener
import com.zlfcapp.ad.csj.TogetherAdCsj
import com.zlfcapp.ad.csj.utils.SplashClickEyeManager
import com.zlfcapp.ad.core.utils.UIUtils
import java.lang.ref.SoftReference
import kotlin.math.roundToInt

/**
 *
 * Created by Matthew Chen on 2020/11/25.
 */
abstract class CsjProviderSplash : CsjProviderReward() {

    private lateinit var mSplashClickEyeManager: SplashClickEyeManager
    private var mTimer: CountDownTimer? = null

    private var mListener: SplashListener? = null
    private var mAdProviderType: String? = null


    private var mSplashAd: TTSplashAd? = null
    override fun loadOnlySplashAd(
        activity: FragmentActivity,
        adProviderType: String,
        alias: String,
        listener: SplashListener
    ) {

        mListener = listener
        mAdProviderType = adProviderType

        callbackSplashStartRequest(adProviderType, alias, listener)

        val adSlotBuilder = AdSlot.Builder()
        adSlotBuilder.setCodeId(TogetherAdCsj.idMapCsj[alias])
        adSlotBuilder.setAdLoadType(TogetherAdCsj.adLoadType)
        adSlotBuilder.setExpressViewAcceptedSize(
            CsjProvider.Splash.imageAcceptedSizeWidthDp,
            CsjProvider.Splash.imageAcceptedSizeHeightDp
        )
        adSlotBuilder.setImageAcceptedSize(
            CsjProvider.Splash.imageAcceptedSizeWidth,
            CsjProvider.Splash.imageAcceptedSizeHeight
        )
        TogetherAdCsj.mTTAdManager.createAdNative(activity)
            .loadSplashAd(adSlotBuilder.build(), object : TTAdNative.SplashAdListener {
                override fun onSplashAdLoad(splashAd: TTSplashAd?) {
                    if (splashAd == null) {
                        callbackSplashFailed(
                            adProviderType,
                            alias,
                            listener,
                            null,
                            "请求成功，但是返回的广告为null"
                        )
                        return
                    }

                    callbackSplashLoaded(adProviderType, alias, listener)

                    mSplashAd = splashAd

                    mSplashAd?.setSplashInteractionListener(object :
                        TTSplashAd.AdInteractionListener {
                        override fun onAdClicked(view: View?, p1: Int) {
                            callbackSplashClicked(adProviderType, listener)
                        }

                        override fun onAdSkip() {
                            CsjProvider.Splash.customSkipView = null
                            callbackSplashDismiss(adProviderType, listener)
                        }

                        override fun onAdShow(p0: View?, p1: Int) {
                            callbackSplashExposure(adProviderType, listener)
                        }

                        override fun onAdTimeOver() {
                            CsjProvider.Splash.customSkipView = null
                            callbackSplashDismiss(adProviderType, listener)
                        }
                    })
                }

                override fun onTimeout() {
                    callbackSplashFailed(adProviderType, alias, listener, null, "请求超时了")
                }

                override fun onError(errorCode: Int, errorMsg: String?) {
                    callbackSplashFailed(adProviderType, alias, listener, errorCode, errorMsg)
                }
            }, CsjProvider.Splash.maxFetchDelay)//超时时间，demo 为 3000
    }

    override fun showSplashAd(container: ViewGroup): Boolean {

        if (mSplashAd?.splashView == null) {
            return false
        }

        container.removeAllViews()
        container.addView(mSplashAd!!.splashView)

        val customSkipView = CsjProvider.Splash.customSkipView
        val skipView = customSkipView?.onCreateSkipView(container.context)

        if (customSkipView != null) {
            mSplashAd?.setNotAllowSdkCountdown()
            skipView?.run {
                container.addView(this, customSkipView.getLayoutParams())
                setOnClickListener {
                    mTimer?.cancel()
                    if (mAdProviderType != null && mListener != null) {
                        CsjProvider.Splash.customSkipView = null
                        callbackSplashDismiss(mAdProviderType!!, mListener!!)
                    }
                }
            }

            //开始倒计时
            mTimer?.cancel()
            mTimer = object : CountDownTimer(5000, 1000) {
                override fun onFinish() {
                    if (mAdProviderType != null && mListener != null) {
                        CsjProvider.Splash.customSkipView = null
                        callbackSplashDismiss(mAdProviderType!!, mListener!!)
                    }
                }

                override fun onTick(millisUntilFinished: Long) {
                    val second = (millisUntilFinished / 1000f).roundToInt()
                    customSkipView.handleTime(second)
                }
            }
            mTimer?.start()
        }

        return true
    }

    override fun loadAndShowSplashAd(
        activity: FragmentActivity,
        adProviderType: String,
        alias: String,
        container: ViewGroup,
        listener: SplashListener
    ) {

        callbackSplashStartRequest(adProviderType, alias, listener)

        val customSkipView = CsjProvider.Splash.customSkipView
        val skipView = customSkipView?.onCreateSkipView(activity)

        val adSlotBuilder = AdSlot.Builder()
        adSlotBuilder.setAdLoadType(TogetherAdCsj.adLoadType)
        adSlotBuilder.setCodeId(TogetherAdCsj.idMapCsj[alias])
        adSlotBuilder.setExpressViewAcceptedSize(
            CsjProvider.Splash.imageAcceptedSizeWidthDp,
            CsjProvider.Splash.imageAcceptedSizeHeightDp
        )
        adSlotBuilder.setImageAcceptedSize(
            CsjProvider.Splash.imageAcceptedSizeWidth,
            CsjProvider.Splash.imageAcceptedSizeHeight
        )
        TogetherAdCsj.mTTAdManager.createAdNative(activity)
            .loadSplashAd(adSlotBuilder.build(), object : TTAdNative.SplashAdListener {
                override fun onSplashAdLoad(splashAd: TTSplashAd?) {

                    if (splashAd == null) {
                        callbackSplashFailed(
                            adProviderType,
                            alias,
                            listener,
                            null,
                            "请求成功，但是返回的广告为null"
                        )
                        return
                    }

                    callbackSplashLoaded(adProviderType, alias, listener)

                    container.removeAllViews()
                    container.addView(splashAd.splashView)

                    splashAd.setSplashInteractionListener(object :
                        TTSplashAd.AdInteractionListener {
                        override fun onAdClicked(view: View?, p1: Int) {
                            callbackSplashClicked(adProviderType, listener)
                        }

                        override fun onAdSkip() {
                            CsjProvider.Splash.customSkipView = null
                            callbackSplashDismiss(adProviderType, listener)
                        }

                        override fun onAdShow(p0: View?, p1: Int) {
                            callbackSplashExposure(adProviderType, listener)
                        }

                        override fun onAdTimeOver() {
                            CsjProvider.Splash.customSkipView = null
                            callbackSplashDismiss(adProviderType, listener)
                        }
                    })

                    //自定义跳过按钮和计时逻辑
                    if (customSkipView != null) {
                        splashAd.setNotAllowSdkCountdown()
                        skipView?.run {
                            container.addView(this, customSkipView.getLayoutParams())
                            setOnClickListener {
                                mTimer?.cancel()
                                CsjProvider.Splash.customSkipView = null
                                callbackSplashDismiss(adProviderType, listener)
                            }
                        }

                        //开始倒计时
                        mTimer?.cancel()
                        mTimer = object : CountDownTimer(5000, 1000) {
                            override fun onFinish() {
                                CsjProvider.Splash.customSkipView = null
                                callbackSplashDismiss(adProviderType, listener)
                            }

                            override fun onTick(millisUntilFinished: Long) {
                                val second = (millisUntilFinished / 1000f).roundToInt()
                                customSkipView.handleTime(second)
                            }
                        }
                        mTimer?.start()
                    }
                }

                override fun onTimeout() {
                    callbackSplashFailed(adProviderType, alias, listener, null, "请求超时了")
                }

                override fun onError(errorCode: Int, errorMsg: String?) {
                    callbackSplashFailed(adProviderType, alias, listener, errorCode, errorMsg)
                }
            }, CsjProvider.Splash.maxFetchDelay)//超时时间，demo 为 3000
    }

    override fun loadSplashEye(
        activity: FragmentActivity,
        adProviderType: String,
        alias: String,
        container: ViewGroup,
        listener: EyesSplashListener
    ) {
        callbackSplashStartRequest(adProviderType, alias, listener)

        val customSkipView = CsjProvider.Splash.customSkipView
        val skipView = customSkipView?.onCreateSkipView(activity)

        val adSlotBuilder = AdSlot.Builder()
        adSlotBuilder.setAdLoadType(TogetherAdCsj.adLoadType)
        adSlotBuilder.setCodeId(TogetherAdCsj.idMapCsj[alias])
        adSlotBuilder.setExpressViewAcceptedSize(
            CsjProvider.Splash.imageAcceptedSizeWidthDp,
            CsjProvider.Splash.imageAcceptedSizeHeightDp
        )
        adSlotBuilder.setImageAcceptedSize(
            CsjProvider.Splash.imageAcceptedSizeWidth,
            CsjProvider.Splash.imageAcceptedSizeHeight
        )
        val splashAd = TogetherAdCsj.mTTAdManager.createAdNative(activity);
        splashAd.loadSplashAd(adSlotBuilder.build(), object : TTAdNative.SplashAdListener {
            override fun onSplashAdLoad(splashAd: TTSplashAd?) {
                if (splashAd == null) {
                    callbackSplashFailed(
                        adProviderType,
                        alias,
                        listener,
                        null,
                        "请求成功，但是返回的广告为null"
                    )
                    return
                }
                callbackSplashLoaded(adProviderType, alias, listener)
                initSplashClickEyeData(splashAd, container, activity,listener)
                container.removeAllViews()
                container.addView(splashAd.splashView)

                splashAd.setSplashInteractionListener(object :
                    TTSplashAd.AdInteractionListener {
                    override fun onAdClicked(view: View?, p1: Int) {
                        callbackSplashClicked(adProviderType, listener)
                    }

                    override fun onAdSkip() {
                        CsjProvider.Splash.customSkipView = null
                        callbackSplashDismiss(adProviderType, listener)
                    }

                    override fun onAdShow(p0: View?, p1: Int) {
                        callbackSplashExposure(adProviderType, listener)
                    }

                    override fun onAdTimeOver() {
                        CsjProvider.Splash.customSkipView = null
                        callbackSplashDismiss(adProviderType, listener)
                    }
                })

                //自定义跳过按钮和计时逻辑
                if (customSkipView != null) {
                    splashAd.setNotAllowSdkCountdown()
                    skipView?.run {
                        container.addView(this, customSkipView.getLayoutParams())
                        setOnClickListener {
                            mTimer?.cancel()
                            CsjProvider.Splash.customSkipView = null
                            callbackSplashDismiss(adProviderType, listener)
                        }
                    }

                    //开始倒计时
                    mTimer?.cancel()
                    mTimer = object : CountDownTimer(5000, 1000) {
                        override fun onFinish() {
                            CsjProvider.Splash.customSkipView = null
                            callbackSplashDismiss(adProviderType, listener)
                        }

                        override fun onTick(millisUntilFinished: Long) {
                            val second = (millisUntilFinished / 1000f).roundToInt()
                            customSkipView.handleTime(second)
                        }
                    }
                    mTimer?.start()
                }
            }

            override fun onTimeout() {
                callbackSplashFailed(adProviderType, alias, listener, null, "请求超时了")
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                callbackSplashFailed(adProviderType, alias, listener, errorCode, errorMsg)
            }
        }, CsjProvider.Splash.maxFetchDelay)//超时时间，demo 为 3000

    }

    private fun initSplashClickEyeData(
        splashAd: TTSplashAd?,
        mSplashContainer: View?,
        activity: FragmentActivity,
        listener: EyesSplashListener
    ) {
        if (splashAd == null || mSplashContainer == null) {
            return
        }
        val mActivity: SoftReference<FragmentActivity> = SoftReference(activity)
        splashAd.setSplashClickEyeListener(object : ISplashClickEyeListener {

            fun finishActivity() {
                if (mActivity.get() == null) {
                    return
                }
                mActivity.get()?.finish()
            }

            override fun onSplashClickEyeAnimationStart() {
                Log.e("HJ", "onSplashClickEyeAnimationStart 开始执行动画")
                startSplashAnimationStart()
            }

            override fun onSplashClickEyeAnimationFinish() {
                Log.e("HJ", "onSplashClickEyeAnimationFinish")
                //sdk关闭了了点睛悬浮窗
                val splashClickEyeManager = SplashClickEyeManager.getInstance()
                val isSupport = splashClickEyeManager.isSupportSplashClickEye
                if (isSupport) {
                    finishActivity()
                }
                listener.eyesClose()
                splashClickEyeManager.clearSplashStaticData()
            }

            override fun isSupportSplashClickEye(isSupport: Boolean): Boolean {
                val splashClickEyeManager = SplashClickEyeManager.getInstance()
                splashClickEyeManager.isSupportSplashClickEye = isSupport
                Log.e("HJ", "isSupportSplashClickEye=>$isSupport")
                return false
            }

            private fun startSplashAnimationStart() {
                if (mActivity.get() == null || mSplashAd == null) {
                    return
                }
                val splashClickEyeManager = SplashClickEyeManager.getInstance()
                val content: ViewGroup = mActivity.get()!!.findViewById(android.R.id.content)
                splashClickEyeManager.startSplashClickEyeAnimation(
                    mSplashContainer,
                    content,
                    content,
                    object : SplashClickEyeManager.AnimationCallBack {
                        override fun animationStart(animationTime: Int) {
                        }

                        override fun animationEnd() {
                            if (mSplashAd != null) {
                                mSplashAd!!.splashClickEyeAnimationFinish()
                            }
                        }
                    })
            }
        })
    }

    override fun loadMainSplashEye(
        activity: FragmentActivity,
        listener: EyesSplashListener
    ) {
        val splashClickEyeManager = SplashClickEyeManager.getInstance()
        val isSupportSplashClickEye = splashClickEyeManager.isSupportSplashClickEye
        if (!isSupportSplashClickEye) {
            splashClickEyeManager.clearSplashStaticData()
            return
        }
        val splashClickEyeView: View? = addSplashClickEyeView(activity)
        if (splashClickEyeView != null) {
            activity.overridePendingTransition(0, 0)
        }
        var mSplashView: SoftReference<View?>? = SoftReference(splashClickEyeView)
        val splashAd = splashClickEyeManager.splashAd
        var mSplashAd: SoftReference<TTSplashAd>? = SoftReference(splashAd)
        splashAd?.setSplashClickEyeListener(object : ISplashClickEyeListener {
            override fun onSplashClickEyeAnimationStart() {

            }

            override fun onSplashClickEyeAnimationFinish() {
                //接收点击关闭按钮的事件将开屏点睛移除。
                if (mSplashView != null && mSplashView?.get() != null) {
                    mSplashView?.get()?.visibility = View.GONE
                    UIUtils.removeFromParent(mSplashView?.get())
                    mSplashView = null
                    mSplashAd = null
                }
                SplashClickEyeManager.getInstance().clearSplashStaticData()
                listener.eyesClose()
            }

            override fun isSupportSplashClickEye(p0: Boolean): Boolean {
                return false
            }
        })
    }

    private fun addSplashClickEyeView(activity: FragmentActivity): View? {
        val splashClickEyeManager = SplashClickEyeManager.getInstance()
        val splashAd = splashClickEyeManager.splashAd
        return splashClickEyeManager.startSplashClickEyeAnimationInTwoActivity(activity.window.decorView as ViewGroup,
            activity.findViewById(android.R.id.content) as ViewGroup?,
            object : SplashClickEyeManager.AnimationCallBack {
                override fun animationStart(animationTime: Int) {
                }

                override fun animationEnd() {
                    splashAd.splashClickEyeAnimationFinish()
                }
            })
    }
}