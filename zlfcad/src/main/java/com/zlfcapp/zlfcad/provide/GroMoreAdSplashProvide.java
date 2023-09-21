package com.zlfcapp.zlfcad.provide;

import android.app.Activity;
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
import com.zlfcapp.zlfcad.AdCustomManager;
import com.zlfcapp.zlfcad.R;
import com.zlfcapp.zlfcad.core.AdSplashProvide;
import com.zlfcapp.zlfcad.core.BaseProvide;
import com.zlfcapp.zlfcad.listener.SplashAdListener;
import com.zlfcapp.zlfcad.utils.UIUtils;

/**
 * 开屏管理类。
 * 只需要复制粘贴到项目中，通过回调处理相应的业务逻辑即可使用完成广告加载&展示
 */
public class GroMoreAdSplashProvide extends BaseProvide {
    private static final String TAG = GroMoreAdSplashProvide.class.getSimpleName();

    private CSJSplashAd mCsjSplashAd;
    private Activity mActivity;

    private final MediationSplashRequestInfo requestInfo;
    private final SplashAdListener mSplashAdLoadCallback;

    /**
     * 管理类构造函数
     */
    public GroMoreAdSplashProvide(Activity activity, MediationSplashRequestInfo requestInfo,
                                  SplashAdListener splashAdListener) {
        super(activity);
        this.requestInfo = requestInfo;
        mSplashAdLoadCallback = splashAdListener;
    }


    @Override
    public void loadAd() {
        String adUnitId = AdCustomManager.getConfig().getGroMoreSplashAdId();
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
                if (mActivity == null || mActivity.isFinishing()) {
                    Log.e(TAG, "广告加载了但是无展示的容器");
                    return;
                }
                setAdShow(true);
                mCsjSplashAd = ad;
                mSplashAdLoadCallback.onAdLoaded();
                mCsjSplashAd.setSplashAdListener(new CSJSplashAd.SplashAdListener() {
                    @Override
                    public void onSplashAdShow(CSJSplashAd csjSplashAd) {
                        mSplashAdLoadCallback.onAdExposure();
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
            }

            @Override
            public void onSplashRenderFail(CSJSplashAd csjSplashAd, CSJAdError csjAdError) {
                mSplashAdLoadCallback.onAdFailed(csjAdError.getCode(), csjAdError.getMsg());
            }
        }, 4000);
    }

    @Override
    public void showAd(ViewGroup contain) {
        if (mCsjSplashAd != null) {
            View splash = mCsjSplashAd.getSplashView();
            UIUtils.removeFromParent(splash);
            contain.removeAllViews();
            contain.addView(splash);
        }
    }

    /**
     * 在Activity onDestroy中需要调用清理资源
     */
    @Override
    public void destroy() {
        if (mCsjSplashAd != null && mCsjSplashAd.getMediationManager() != null) {
            mCsjSplashAd.getMediationManager().destroy();
        }
        mCsjSplashAd = null;
        mActivity = null;
    }

}
