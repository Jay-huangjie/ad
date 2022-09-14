package com.zlfcapp.ad.csj.provider

import androidx.fragment.app.FragmentActivity
import com.bytedance.sdk.openadsdk.AdSlot
import com.bytedance.sdk.openadsdk.TTAdNative
import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd
import com.zlfcapp.ad.core.listener.FullVideoListener
import com.zlfcapp.ad.core.utils.logd
import com.zlfcapp.ad.csj.TogetherAdCsj

/**
 *
 * Created by Matthew Chen on 2020/12/1.
 */
abstract class CsjProviderFullVideo : CsjProviderBanner() {

    private var mFllScreenVideoAd: TTFullScreenVideoAd? = null
    override fun requestFullVideoAd(activity: FragmentActivity, adProviderType: String, alias: String, listener: FullVideoListener) {

        callbackFullVideoStartRequest(adProviderType, alias, listener)

        val adSlotBuilder = AdSlot.Builder()
        adSlotBuilder.setCodeId(TogetherAdCsj.idMapCsj[alias])
        adSlotBuilder.setAdLoadType(TogetherAdCsj.adLoadType)
        //模板广告需要设置期望个性化模板广告的大小,单位dp,激励视频场景，只要设置的值大于0即可且仅是模板渲染的代码位ID使用，非模板渲染代码位切勿使用
        if (CsjProvider.FullVideo.isExpress) {
            adSlotBuilder.setExpressViewAcceptedSize(500f, 500f)
        }
        adSlotBuilder.setSupportDeepLink(CsjProvider.FullVideo.supportDeepLink)
        adSlotBuilder.setOrientation(CsjProvider.FullVideo.orientation)//必填参数，期望视频的播放方向：TTAdConstant.HORIZONTAL 或 TTAdConstant.VERTICAL

        TogetherAdCsj.mTTAdManager.createAdNative(activity).loadFullScreenVideoAd(adSlotBuilder.build(), object : TTAdNative.FullScreenVideoAdListener {
            override fun onFullScreenVideoAdLoad(fullScreenVideoAd: TTFullScreenVideoAd?) {
                mFllScreenVideoAd = fullScreenVideoAd

                mFllScreenVideoAd?.setFullScreenVideoAdInteractionListener(object : TTFullScreenVideoAd.FullScreenVideoAdInteractionListener {
                    override fun onSkippedVideo() {}

                    override fun onAdShow() {
                        callbackFullVideoShow(adProviderType, listener)
                    }

                    override fun onAdVideoBarClick() {
                        callbackFullVideoClicked(adProviderType, listener)
                    }

                    override fun onVideoComplete() {
                        callbackFullVideoComplete(adProviderType, listener)
                    }

                    override fun onAdClose() {
                        callbackFullVideoClosed(adProviderType, listener)
                    }
                })

                callbackFullVideoLoaded(adProviderType, alias, listener)
            }

            override fun onFullScreenVideoCached() {}

            override fun onFullScreenVideoCached(videoAd: TTFullScreenVideoAd?) {
                callbackFullVideoCached(adProviderType, listener)
            }

            override fun onError(errorCode: Int, errorMsg: String?) {
                callbackFullVideoFailed(adProviderType, alias, listener, errorCode, errorMsg)
            }
        })
    }

    override fun showFullVideoAd(activity: FragmentActivity): Boolean {
        "过期时间：${mFllScreenVideoAd?.expirationTimestamp}".logd(tag)
        if (mFllScreenVideoAd?.expirationTimestamp ?: 0 <= System.currentTimeMillis()) {
            return false
        }
        val ritScenes = CsjProvider.FullVideo.ritScenes
        if (ritScenes != null) {
            mFllScreenVideoAd?.showFullScreenVideoAd(activity, ritScenes, null)
        } else {
            mFllScreenVideoAd?.showFullScreenVideoAd(activity)
        }
        mFllScreenVideoAd = null
        return true
    }
}