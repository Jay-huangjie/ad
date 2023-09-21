package com.mediation.ads.provide;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.CSJAdError;
import com.bytedance.sdk.openadsdk.CSJSplashAd;
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
public class AdSplashProvide extends BaseProvide {
    private static final String TAG = AdSplashProvide.class.getSimpleName();
    private final AdListener mSplashAdLoadCallback;

    private CSJSplashAd mCsjSplashAd;
    private final int ad_time_out;
    private final MediationSplashRequestInfo requestInfo;
    private ViewGroup adContain;


    public AdSplashProvide(Activity activity, ViewGroup adContain, MediationSplashRequestInfo requestInfo,
                           AdListener splashAdListener) {
        this(activity, adContain, requestInfo, splashAdListener, 10000);
    }

    public AdSplashProvide(Activity activity, ViewGroup adContain, MediationSplashRequestInfo requestInfo,
                           AdListener splashAdListener, int ad_time_out) {
        super(activity);
        this.ad_time_out = ad_time_out;
        this.requestInfo = requestInfo;
        mSplashAdLoadCallback = splashAdListener;
        this.adContain = adContain;
    }

    @Override
    protected void init(String id) {
        Handler mHandler = new Handler();
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
        adNative.loadSplashAd(adslot, new TTAdNative.CSJSplashAdListener() {
            @Override
            public void onSplashLoadSuccess() {

            }

            @Override
            public void onSplashLoadFail(CSJAdError csjAdError) {
                mSplashAdLoadCallback.onAdFailed(csjAdError.getCode(), csjAdError.getMsg());
            }

            @Override
            public void onSplashRenderSuccess(CSJSplashAd ad) {
                if (ad == null) {
                    mSplashAdLoadCallback.onAdFailed(0, "请求成功，但是返回的广告为null");
                    return;
                }
                if (adContain == null || mActivity == null || mActivity.isFinishing()) {
                    log("广告加载了但是无展示的容器");
                    return;
                }
                mCsjSplashAd = ad;
                mSplashAdLoadCallback.onAdLoaded();
                mCsjSplashAd.setSplashAdListener(new CSJSplashAd.SplashAdListener() {
                    @Override
                    public void onSplashAdShow(CSJSplashAd csjSplashAd) {
                        mSplashAdLoadCallback.onAdExposure();
                        mHandler.removeCallbacksAndMessages(null);
                    }

                    @Override
                    public void onSplashAdClick(CSJSplashAd csjSplashAd) {
                        mSplashAdLoadCallback.onAdClicked();
                    }

                    @Override
                    public void onSplashAdClose(CSJSplashAd csjSplashAd, int i) {
                        mSplashAdLoadCallback.onAdDismissed();
                    }
                });
                showAd();
            }

            @Override
            public void onSplashRenderFail(CSJSplashAd csjSplashAd, CSJAdError csjAdError) {
                mSplashAdLoadCallback.onAdFailed(csjAdError.getCode(), csjAdError.getMsg());
            }
        }, ad_time_out);
        Runnable mTimeOutCheckRunnable = () -> {
            if (mActivity != null && !mActivity.isFinishing()) {
                if (mSplashAdLoadCallback != null) {
                    mSplashAdLoadCallback.onAdFailed(-1, "广告加载超时");
                }
            }
        };
        mHandler.postDelayed(mTimeOutCheckRunnable, ad_time_out);
    }

    @Override
    protected void onDestroy() {
        adContain.removeAllViews();
        if (mCsjSplashAd != null && mCsjSplashAd.getMediationManager() != null) {
            mCsjSplashAd.getMediationManager().destroy();
        }
        mCsjSplashAd = null;
        mActivity = null;
    }


    public void showAd() {
        View splashView = mCsjSplashAd.getSplashView();
        UIUtils.removeFromParent(splashView);
        adContain.removeAllViews();
        adContain.addView(splashView);
    }

}
