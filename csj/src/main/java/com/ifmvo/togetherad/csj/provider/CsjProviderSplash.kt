package com.ifmvo.togetherad.csj.provider

import android.app.Activity
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.bytedance.sdk.openadsdk.*
import com.ifmvo.togetherad.core.listener.EyesSplashListener
import com.ifmvo.togetherad.core.listener.SplashListener
import com.ifmvo.togetherad.csj.TogetherAdCsj
import com.ifmvo.togetherad.csj.utils.SplashClickEyeManager
import com.ifmvo.togetherad.core.utils.UIUtils
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
        activity: Activity,
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
                            "???????????????????????????????????????null"
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
                    callbackSplashFailed(adProviderType, alias, listener, null, "???????????????")
                }

                override fun onError(errorCode: Int, errorMsg: String?) {
                    callbackSplashFailed(adProviderType, alias, listener, errorCode, errorMsg)
                }
            }, CsjProvider.Splash.maxFetchDelay)//???????????????demo ??? 3000
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

            //???????????????
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
        activity: Activity,
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
                            "???????????????????????????????????????null"
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

                    //????????????????????????????????????
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

                        //???????????????
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
                    callbackSplashFailed(adProviderType, alias, listener, null, "???????????????")
                }

                override fun onError(errorCode: Int, errorMsg: String?) {
                    callbackSplashFailed(adProviderType, alias, listener, errorCode, errorMsg)
                }
            }, CsjProvider.Splash.maxFetchDelay)//???????????????demo ??? 3000
    }

    override fun loadSplashEye(
        activity: Activity,
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
                        "???????????????????????????????????????null"
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

                //????????????????????????????????????
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

                    //???????????????
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
                callbackSplashFailed(adProviderType, alias, listener, null, "???????????????")
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                callbackSplashFailed(adProviderType, alias, listener, errorCode, errorMsg)
            }
        }, CsjProvider.Splash.maxFetchDelay)//???????????????demo ??? 3000

    }

    private fun initSplashClickEyeData(
        splashAd: TTSplashAd?,
        mSplashContainer: View?,
        activity: Activity,
        listener: EyesSplashListener
    ) {
        if (splashAd == null || mSplashContainer == null) {
            return
        }
        val mActivity: SoftReference<Activity> = SoftReference(activity)
        splashAd.setSplashClickEyeListener(object : ISplashClickEyeListener {

            fun finishActivity() {
                if (mActivity.get() == null) {
                    return
                }
                mActivity.get()?.finish()
            }

            override fun onSplashClickEyeAnimationStart() {
                Log.e("HJ", "onSplashClickEyeAnimationStart ??????????????????")
                startSplashAnimationStart()
            }

            override fun onSplashClickEyeAnimationFinish() {
                Log.e("HJ", "onSplashClickEyeAnimationFinish")
                //sdk???????????????????????????
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
        activity: Activity,
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
                //?????????????????????????????????????????????????????????
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

    private fun addSplashClickEyeView(activity: Activity): View? {
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