package com.mediation.ads.provide;

import android.app.Activity;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTFullScreenVideoAd;
import com.mediation.ads.listener.AdListener;

/**
 * 插全屏管理类。
 * 只需要复制粘贴到项目中，通过回调处理相应的业务逻辑即可使用完成广告加载&展示
 */
public class AdInterstitialProvide extends BaseProvide {

    private TTFullScreenVideoAd mTTFullScreenVideoAd;
    private AdListener mCallback;

    public AdInterstitialProvide(Activity activity, AdListener callback) {
        super(activity);
        mCallback = callback;
    }


    public TTFullScreenVideoAd getTTFullScreenVideoAd() {
        return mTTFullScreenVideoAd;
    }

    public void showAd(TTFullScreenVideoAd ad, boolean isCache) {
        mTTFullScreenVideoAd = ad;
        if (mTTFullScreenVideoAd.getMediationManager().isReady() || !isCache) {
            mTTFullScreenVideoAd.setFullScreenVideoAdInteractionListener(new TTFullScreenVideoAd.FullScreenVideoAdInteractionListener() {
                @Override
                public void onAdShow() {
                    mCallback.onAdExposure();
                }

                @Override
                public void onAdVideoBarClick() {
                    mCallback.onAdClicked();
                }

                @Override
                public void onAdClose() {
                    mCallback.onAdDismissed();
                }

                @Override
                public void onVideoComplete() {

                }

                @Override
                public void onSkippedVideo() {
                    mCallback.onAdDismissed();
                }
            });
            mTTFullScreenVideoAd.showFullScreenVideoAd(mActivity);
        } else {
            mCallback.onAdFailed(-100, "广告加载成功,但是还没准备好");
        }

    }


    @Override
    protected void init(String id, AdSlot adSlot) {
        TTAdNative adNative = TTAdSdk.getAdManager().createAdNative(mActivity);
        if (adSlot == null) {
            adSlot = new AdSlot.Builder()
                    .setCodeId(id)
                    .setOrientation(TTAdConstant.VERTICAL)
                    .build();
        }
        adNative.loadFullScreenVideoAd(adSlot, new TTAdNative.FullScreenVideoAdListener() {
            @Override
            public void onError(int i, String s) {
                mCallback.onAdFailed(i, s);
            }

            @Override
            public void onFullScreenVideoAdLoad(TTFullScreenVideoAd ttFullScreenVideoAd) {
                mCallback.onAdLoaded();
                if (ttFullScreenVideoAd != null) {
                    showAd(ttFullScreenVideoAd, false);
                } else {
                    mCallback.onAdFailed(0, "广告加载成功,但是无广告");
                }
            }

            @Override
            public void onFullScreenVideoCached() {

            }

            @Override
            public void onFullScreenVideoCached(TTFullScreenVideoAd ttFullScreenVideoAd) {
                mCallback.onAdLoaded();
                if (ttFullScreenVideoAd != null) {
                    showAd(ttFullScreenVideoAd, true);
                } else {
                    mCallback.onAdFailed(0, "广告加载成功,但是无广告");
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (mTTFullScreenVideoAd != null && mTTFullScreenVideoAd.getMediationManager() != null) {
            mTTFullScreenVideoAd.getMediationManager().destroy();
        }
        mTTFullScreenVideoAd = null;
        mActivity = null;
    }

}
