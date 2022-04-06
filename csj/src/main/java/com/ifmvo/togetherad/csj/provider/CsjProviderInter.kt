package com.ifmvo.togetherad.csj.provider

import android.app.Activity
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import com.bytedance.sdk.openadsdk.*
import com.ifmvo.togetherad.core.listener.InterListener
import com.ifmvo.togetherad.core.utils.logd
import com.ifmvo.togetherad.core.utils.logi
import com.ifmvo.togetherad.csj.TogetherAdCsj

/**
 *
 * Created by Matthew Chen on 2020/11/2.
 */
abstract class CsjProviderInter : CsjProviderFullVideo() {

    //    private var mTTNativeExpressInterAd: TTNativeExpressAd? = null
    private var mFllScreenVideoAd: TTFullScreenVideoAd? = null
    override fun requestInterAd(
        activity: Activity,
        adProviderType: String,
        alias: String,
        listener: InterListener
    ) {
        destroyInterAd()

        callbackInterStartRequest(adProviderType, alias, listener)

        val adSlotBuilder = AdSlot.Builder()
            .setAdLoadType(TogetherAdCsj.adLoadType)
            .setCodeId(TogetherAdCsj.idMapCsj[alias]) //广告位id
            .setSupportDeepLink(CsjProvider.Inter.supportDeepLink)
            .setExpressViewAcceptedSize(
                CsjProvider.Inter.expressViewAcceptedSizeWidth,
                CsjProvider.Inter.expressViewAcceptedSizeHeight
            )
            .setAdCount(1) //请求广告数量为1到3条
            .setOrientation(CsjProvider.FullVideo.orientation)
        TogetherAdCsj.mTTAdManager.createAdNative(activity)
            .loadFullScreenVideoAd(adSlotBuilder.build(),
                object : TTAdNative.FullScreenVideoAdListener {
                    override fun onError(p0: Int, p1: String?) {
                         Log.e("HJ", "Error:$p1--$p0")
                    }

                    override fun onFullScreenVideoAdLoad(fullScreenVideoAd: TTFullScreenVideoAd?) {
                        Log.e("HJ","onFullScreenVideoAdLoad")
                        mFllScreenVideoAd = fullScreenVideoAd
                        mFllScreenVideoAd?.setFullScreenVideoAdInteractionListener(object : TTFullScreenVideoAd.FullScreenVideoAdInteractionListener {
                            override fun onSkippedVideo() {

                            }

                            override fun onAdShow() {

                            }

                            override fun onAdVideoBarClick() {
                                callbackInterClicked(adProviderType, listener)
                            }

                            override fun onVideoComplete() {
                                callbackInterExpose(adProviderType, listener)
                            }

                            override fun onAdClose() {
                                callbackInterClosed(adProviderType, listener)
                            }
                        })
                    }

                    override fun onFullScreenVideoCached() {
                        Log.e("HJ","onFullScreenVideoCached")
                        callbackInterLoaded(adProviderType, alias, listener)
                    }

                    override fun onFullScreenVideoCached(p0: TTFullScreenVideoAd?) {
                        Log.e("HJ","onFullScreenVideoCached")
                    }

                });
//        TogetherAdCsj.mTTAdManager.createAdNative(activity).loadFullScreenVideoAd(adSlotBuilder.build(), object : TTAdNative.NativeExpressAdListener {
//
//
//
//
//            override fun onNativeExpressAdLoad(adList: MutableList<TTNativeExpressAd>?) {
//                if (adList.isNullOrEmpty()) {
//                    callbackInterFailed(adProviderType, alias, listener, null, "请求成功，但是返回的list为空")
//                    return
//                }
//                callbackInterLoaded(adProviderType, alias, listener)
//
//                mTTNativeExpressInterAd = adList[0]
//                mTTNativeExpressInterAd?.setExpressInteractionListener(object : TTNativeExpressAd.AdInteractionListener {
//                    override fun onAdDismiss() {
//                        callbackInterClosed(adProviderType, listener)
//                    }
//
//                    override fun onAdClicked(p0: View?, p1: Int) {
//                        callbackInterClicked(adProviderType, listener)
//                    }
//
//                    override fun onAdShow(view: View?, p1: Int) {
//                        callbackInterExpose(adProviderType, listener)
//                    }
//
//                    override fun onRenderSuccess(view: View?, p1: Float, p2: Float) {
//                        mTTNativeExpressInterAd?.showInteractionExpressAd(activity)
//                    }
//
//                    override fun onRenderFail(view: View?, errorMsg: String?, errorCode: Int) {
//
//                    }
//                })
//                mTTNativeExpressInterAd?.setDislikeCallback(activity, object : TTAdDislike.DislikeInteractionCallback {
//                    override fun onSelected(p0: Int, p1: String?, enforce: Boolean) {}
//                    override fun onCancel() {}
//                    override fun onShow() {}
//                })
//            }
//
//            override fun onError(errorCode: Int, errorMsg: String?) {
//                callbackInterFailed(adProviderType, alias, listener, errorCode, errorMsg)
//            }
//        })
    }

    override fun showInterAd(activity: Activity) {
        "过期时间：${mFllScreenVideoAd?.expirationTimestamp}".logd(tag)
        if (mFllScreenVideoAd?.expirationTimestamp ?: 0 <= System.currentTimeMillis()) {
            return
        }
        val ritScenes = CsjProvider.FullVideo.ritScenes
        if (ritScenes != null) {
            mFllScreenVideoAd?.showFullScreenVideoAd(activity, ritScenes, null)
        } else {
            mFllScreenVideoAd?.showFullScreenVideoAd(activity)
        }
        mFllScreenVideoAd = null
    }

    override fun destroyInterAd() {
        mFllScreenVideoAd = null
    }

}