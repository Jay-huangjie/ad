package com.ifmvo.togetherad.csj.provider

import android.app.Activity
import android.os.Bundle
import com.bytedance.sdk.openadsdk.*
import com.ifmvo.togetherad.core.listener.RewardListener
import com.ifmvo.togetherad.core.utils.logd
import com.ifmvo.togetherad.csj.TogetherAdCsj

/**
 *
 * Created by Matthew Chen on 2020/11/25.
 */
abstract class CsjProviderReward : CsjProviderNativeExpress() {

    private var mttRewardVideoAd: TTRewardVideoAd? = null
    override fun requestRewardAd(activity: Activity, adProviderType: String, alias: String, listener: RewardListener) {

        callbackRewardStartRequest(adProviderType, alias, listener)

        val adSlotBuilder = AdSlot.Builder()
                .setAdLoadType(TogetherAdCsj.adLoadType)
                .setCodeId(TogetherAdCsj.idMapCsj[alias])
                .setSupportDeepLink(CsjProvider.Reward.supportDeepLink)
                //必传参数，表来标识应用侧唯一用户；若非服务器回调模式或不需sdk透传,可设置为空字符串
                .setUserID(if (CsjProvider.Reward.userID?.isNotEmpty() == true) CsjProvider.Reward.userID else "")
                .setOrientation(CsjProvider.Reward.orientation)  //设置期望视频播放的方向，为TTAdConstant.HORIZONTAL或TTAdConstant.VERTICAL

        if (CsjProvider.Reward.mediaExtra?.isNotEmpty() == true) {
            adSlotBuilder.setMediaExtra(CsjProvider.Reward.mediaExtra)
        }

        if (CsjProvider.Reward.isExpress) {
            //模板广告需要设置期望个性化模板广告的大小,单位dp,激励视频场景，只要设置的值大于0即可
            adSlotBuilder.setExpressViewAcceptedSize(500f, 500f)
        }

        TogetherAdCsj.mTTAdManager.createAdNative(activity).loadRewardVideoAd(adSlotBuilder.build(), object : TTAdNative.RewardVideoAdListener {
            override fun onError(code: Int, message: String?) {
                callbackRewardFailed(adProviderType, alias, listener, code, message)
                mttRewardVideoAd = null
            }

            override fun onRewardVideoCached() {}

            //视频广告加载后的视频文件资源缓存到本地的回调
            override fun onRewardVideoCached(videoAd: TTRewardVideoAd?) {
                callbackRewardVideoCached(adProviderType, listener)
            }

            //视频广告素材加载到，如title,视频url等，不包括视频文件
            override fun onRewardVideoAdLoad(ad: TTRewardVideoAd) {

                mttRewardVideoAd = ad
                mttRewardVideoAd?.setShowDownLoadBar(CsjProvider.Reward.showDownLoadBar)

                val rewardAdListener = object : TTRewardVideoAd.RewardAdInteractionListener {
                    override fun onSkippedVideo() {
                    }

                    override fun onVideoError() {
                    }

                    override fun onAdShow() {
                        callbackRewardShow(adProviderType, listener)
                        callbackRewardExpose(adProviderType, listener)
                    }

                    override fun onAdVideoBarClick() {
                        callbackRewardClicked(adProviderType, listener)
                    }

                    override fun onAdClose() {
                        callbackRewardClosed(adProviderType, listener)
                        mttRewardVideoAd = null
                    }

                    override fun onVideoComplete() {
                        callbackRewardVideoComplete(adProviderType, listener)
                    }

                    override fun onRewardVerify(rewardVerify: Boolean, rewardAmount: Int, rewardName: String?, errorCode: Int, errorMsg: String?) {
                        CsjProvider.Reward.rewardVerify = rewardVerify
                        CsjProvider.Reward.errorCode = errorCode
                        CsjProvider.Reward.errorMsg = errorMsg
                        callbackRewardVerify(adProviderType, listener)
                    }

                    override fun onRewardArrived(p0: Boolean, p1: Int, p2: Bundle?) {

                    }
                }

                mttRewardVideoAd?.setRewardAdInteractionListener(rewardAdListener)
                mttRewardVideoAd?.setRewardPlayAgainInteractionListener(rewardAdListener)

                mttRewardVideoAd?.setDownloadListener(object : TTAppDownloadListener {
                    override fun onIdle() {
                    }

                    override fun onDownloadActive(totalBytes: Long, currBytes: Long, fileName: String?, appName: String?) {
                    }

                    override fun onDownloadPaused(totalBytes: Long, currBytes: Long, fileName: String?, appName: String?) {
                    }

                    override fun onDownloadFailed(totalBytes: Long, currBytes: Long, fileName: String?, appName: String?) {
                    }

                    override fun onDownloadFinished(totalBytes: Long, fileName: String?, appName: String?) {
                    }

                    override fun onInstalled(fileName: String?, appName: String?) {
                    }
                })

                callbackRewardLoaded(adProviderType, alias, listener)
            }
        })
    }

    override fun showRewardAd(activity: Activity): Boolean {
        "过期时间：${mttRewardVideoAd?.expirationTimestamp}".logd(tag)
        if (mttRewardVideoAd?.expirationTimestamp ?: 0 <= System.currentTimeMillis()) {
            return false
        }
        mttRewardVideoAd?.showRewardVideoAd(activity, TTAdConstant.RitScenes.CUSTOMIZE_SCENES, "scenes_test")
        return true
    }

    override fun requestAndShowRewardAd(activity: Activity, adProviderType: String, alias: String, listener: RewardListener) {

        callbackRewardStartRequest(adProviderType, alias, listener)

        val adSlotBuilder = AdSlot.Builder()
                .setAdLoadType(TogetherAdCsj.adLoadType)
                .setCodeId(TogetherAdCsj.idMapCsj[alias])
                .setSupportDeepLink(CsjProvider.Reward.supportDeepLink)
                //必传参数，表来标识应用侧唯一用户；若非服务器回调模式或不需sdk透传,可设置为空字符串
                .setUserID(if (CsjProvider.Reward.userID?.isNotEmpty() == true) CsjProvider.Reward.userID else "")
                .setOrientation(CsjProvider.Reward.orientation)  //设置期望视频播放的方向，为TTAdConstant.HORIZONTAL或TTAdConstant.VERTICAL

        if (CsjProvider.Reward.mediaExtra?.isNotEmpty() == true) {
            adSlotBuilder.setMediaExtra(CsjProvider.Reward.mediaExtra)
        }

        if (CsjProvider.Reward.isExpress) {
            //模板广告需要设置期望个性化模板广告的大小,单位dp,激励视频场景，只要设置的值大于0即可
            adSlotBuilder.setExpressViewAcceptedSize(500f, 500f)
        }

        TogetherAdCsj.mTTAdManager.createAdNative(activity).loadRewardVideoAd(adSlotBuilder.build(), object : TTAdNative.RewardVideoAdListener {
            override fun onError(code: Int, message: String?) {
                callbackRewardFailed(adProviderType, alias, listener, code, message)
                mttRewardVideoAd = null
            }

            override fun onRewardVideoCached() {}

            //视频广告加载后的视频文件资源缓存到本地的回调
            override fun onRewardVideoCached(videoAd: TTRewardVideoAd?) {
                callbackRewardVideoCached(adProviderType, listener)
                showRewardAd(activity)
            }

            //视频广告素材加载到，如title,视频url等，不包括视频文件
            override fun onRewardVideoAdLoad(ad: TTRewardVideoAd) {

                mttRewardVideoAd = ad
                mttRewardVideoAd?.setShowDownLoadBar(CsjProvider.Reward.showDownLoadBar)

                val rewardAdListener = object : TTRewardVideoAd.RewardAdInteractionListener {
                    override fun onSkippedVideo() {
                    }

                    override fun onVideoError() {
                    }

                    override fun onAdShow() {
                        callbackRewardShow(adProviderType, listener)
                        callbackRewardExpose(adProviderType, listener)
                    }

                    override fun onAdVideoBarClick() {
                        callbackRewardClicked(adProviderType, listener)
                    }

                    override fun onAdClose() {
                        callbackRewardClosed(adProviderType, listener)
                        mttRewardVideoAd = null
                    }

                    override fun onVideoComplete() {
                        callbackRewardVideoComplete(adProviderType, listener)
                    }

                    override fun onRewardVerify(rewardVerify: Boolean, rewardAmount: Int, rewardName: String?, errorCode: Int, errorMsg: String?) {
                        CsjProvider.Reward.rewardVerify = rewardVerify
                        CsjProvider.Reward.errorCode = errorCode
                        CsjProvider.Reward.errorMsg = errorMsg
                        callbackRewardVerify(adProviderType, listener)
                    }

                    override fun onRewardArrived(p0: Boolean, p1: Int, p2: Bundle?) {

                    }
                }

                mttRewardVideoAd?.setRewardAdInteractionListener(rewardAdListener)
                mttRewardVideoAd?.setRewardPlayAgainInteractionListener(rewardAdListener)

                mttRewardVideoAd?.setDownloadListener(object : TTAppDownloadListener {
                    override fun onIdle() {
                    }

                    override fun onDownloadActive(totalBytes: Long, currBytes: Long, fileName: String?, appName: String?) {
                    }

                    override fun onDownloadPaused(totalBytes: Long, currBytes: Long, fileName: String?, appName: String?) {
                    }

                    override fun onDownloadFailed(totalBytes: Long, currBytes: Long, fileName: String?, appName: String?) {
                    }

                    override fun onDownloadFinished(totalBytes: Long, fileName: String?, appName: String?) {
                    }

                    override fun onInstalled(fileName: String?, appName: String?) {
                    }
                })

                callbackRewardLoaded(adProviderType, alias, listener)
            }
        })
    }

}