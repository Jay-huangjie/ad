package com.mediation.ads.provide;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTSplashAd;
import com.bytedance.sdk.openadsdk.mediation.ad.MediationAdSlot;
import com.bytedance.sdk.openadsdk.mediation.ad.MediationSplashRequestInfo;
import com.mediation.ads.UIUtils;
import com.mediation.ads.listener.AdListener;

/**
 * 开屏管理类。
 * 只需要复制粘贴到项目中，通过回调处理相应的业务逻辑即可使用完成广告加载&展示
 */
public class AdSplashProvide extends BaseProvide{
    private static final String TAG = AdSplashProvide.class.getSimpleName();
    private final AdListener mSplashAdLoadCallback;

    private TTSplashAd mSplashAd;
    private Activity mActivity;
    //开屏广告加载超时时间,建议大于1000,这里为了冷启动第一次加载到广告并且展示,示例设置了2000ms
    private static final int AD_TIME_OUT = 4000;
    private final MediationSplashRequestInfo requestInfo;
    private ViewGroup adContain;


    public AdSplashProvide(Activity activity, ViewGroup adContain, MediationSplashRequestInfo requestInfo,
                           AdListener splashAdListener) {
        mActivity = activity;
        this.requestInfo = requestInfo;
        mSplashAdLoadCallback = splashAdListener;
        this.adContain = adContain;
    }

    /**
     * 获取开屏广告对象
     */
    public TTSplashAd getSplashAd() {
        return mSplashAd;
    }


    @Override
    protected void init(String id) {
        TTAdNative adNative = TTAdSdk.getAdManager().createAdNative(mActivity);
        AdSlot adslot = new AdSlot.Builder().
                setCodeId(id)
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
                    showAd();
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

    @Override
    protected void onDestroy() {
        adContain.removeAllViews();
        mSplashAd = null;
        mActivity = null;
    }


    public void showAd() {
        adContain.addView(mSplashAd.getSplashView());
    }

}
