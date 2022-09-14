package com.zlfcapp.ad.csj.provider

import androidx.fragment.app.FragmentActivity
import com.bytedance.sdk.openadsdk.*
import com.zlfcapp.ad.core.listener.InterListener
import com.zlfcapp.ad.core.utils.logd
import com.zlfcapp.ad.csj.TogetherAdCsj

/**
 *
 * Created by Matthew Chen on 2020/11/2.
 */
abstract class CsjProviderInter : CsjProviderFullVideo() {

    private var mFllScreenVideoAd: TTFullScreenVideoAd? = null
    override fun requestInterAd(
        activity: FragmentActivity,
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
                        callbackInterFailed(adProviderType, alias, listener, p0, p1)
                    }

                    override fun onFullScreenVideoAdLoad(fullScreenVideoAd: TTFullScreenVideoAd?) {
                        mFllScreenVideoAd = fullScreenVideoAd
                        mFllScreenVideoAd?.setFullScreenVideoAdInteractionListener(object :
                            TTFullScreenVideoAd.FullScreenVideoAdInteractionListener {
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

                    }

                    override fun onFullScreenVideoCached(p0: TTFullScreenVideoAd?) {
                        callbackInterLoaded(adProviderType, alias, listener)
                    }
                });
    }

    override fun showInterAd(activity: FragmentActivity) {
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