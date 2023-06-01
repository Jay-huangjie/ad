package com.zlfcapp.zlfcad.provide;

import android.app.Activity;
import android.util.Log;
import android.view.View;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.bytedance.sdk.openadsdk.mediation.ad.MediationAdSlot;
import com.bytedance.sdk.openadsdk.mediation.ad.MediationSplashRequestInfo;
import com.zlfcapp.zlfcad.R;
import com.zlfcapp.zlfcad.core.AdSplashProvide;
import com.zlfcapp.zlfcad.listener.SplashAdListener;
import com.zlfcapp.zlfcad.utils.UIUtils;

/**
 * 开屏管理类。
 * 只需要复制粘贴到项目中，通过回调处理相应的业务逻辑即可使用完成广告加载&展示
 */
public class GroMoreAdSplashProvide {
    private static final String TAG = GroMoreAdSplashProvide.class.getSimpleName();

    private TTSplashAd mSplashAd;
    private Activity mActivity;

    private final MediationSplashRequestInfo requestInfo;
    private final SplashAdListener mSplashAdLoadCallback;

    /**
     * 管理类构造函数
     */
    public GroMoreAdSplashProvide(Activity activity, MediationSplashRequestInfo requestInfo,
                                  SplashAdListener splashAdListener) {
        mActivity = activity;
        this.requestInfo = requestInfo;
        mSplashAdLoadCallback = splashAdListener;
    }

    public TTSplashAd getSplashAd() {
        return mSplashAd;
    }

    /**
     * 加载开屏广告
     *
     * @param adUnitId 广告位ID
     */
    public void loadSplashAd(String adUnitId) {
        TTAdNative adNative = TTAdSdk.getAdManager().createAdNative(mActivity);
        AdSlot adslot = new AdSlot.Builder().
                setCodeId(adUnitId)
                .setImageAcceptedSize(UIUtils.getScreenWidthInPx(mActivity), UIUtils.getScreenHeightInPx(mActivity))
                .setMediationAdSlot(
                        new MediationAdSlot.Builder()
                                .setMediationSplashRequestInfo(requestInfo)
                                .build()
                )
                .build();
        adNative.loadSplashAd(adslot, new TTAdNative.SplashAdListener() {
            @Override
            public void onError(int i, String s) {
                mSplashAdLoadCallback.onAdFailed(i, s);
            }

            @Override
            public void onTimeout() {
                mSplashAdLoadCallback.onAdFailed(0, "请求超时了");
            }

            @Override
            public void onSplashAdLoad(TTSplashAd ttSplashAd) {
                if (ttSplashAd != null) {
                    mSplashAd = ttSplashAd;
                    mSplashAdLoadCallback.onAdLoaded();
                    ttSplashAd.setSplashInteractionListener(new TTSplashAd.AdInteractionListener() {
                        @Override
                        public void onAdClicked(View view, int i) {
                            mSplashAdLoadCallback.onAdClicked();
                        }

                        @Override
                        public void onAdShow(View view, int i) {
                            mSplashAdLoadCallback.onAdExposure();
                        }

                        @Override
                        public void onAdSkip() {
                            mSplashAdLoadCallback.onAdDismissed();
                        }

                        @Override
                        public void onAdTimeOver() {
                            mSplashAdLoadCallback.onAdDismissed();
                        }
                    });
                } else {
                    mSplashAdLoadCallback.onAdFailed(0, "请求成功，但是返回的广告为null");
                }
            }
        });
    }


    /**
     * 在Activity onDestroy中需要调用清理资源
     */
    public void destroy() {
        mSplashAd = null;
        mActivity = null;
    }

}
