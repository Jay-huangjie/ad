package com.zlfcapp.zlfcad.core;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;

import com.beizi.fusion.AdListener;
import com.bytedance.sdk.openadsdk.mediation.MediationConstant;
import com.bytedance.sdk.openadsdk.mediation.ad.MediationSplashRequestInfo;
import com.zlfcapp.zlfcad.AdCustomManager;
import com.zlfcapp.zlfcad.listener.SplashAdListener;
import com.zlfcapp.zlfcad.provide.BeiziAdSplashProvide;
import com.zlfcapp.zlfcad.provide.GroMoreAdSplashProvide;
import com.zlfcapp.zlfcad.utils.UIUtils;


/**
 * 开屏管理类。
 * 只需要复制粘贴到项目中，通过回调处理相应的业务逻辑即可使用完成广告加载&展示
 */
public class AdSplashProvide {
    private static final String TAG = AdSplashProvide.class.getSimpleName();
    private GroMoreAdSplashProvide groMoreProvide;
    private BeiziAdSplashProvide beiziProvide;
    private Activity activity;
    private boolean adOpen;
    private Handler mHandler;

    private AdCustomManager.InitCallback callback;

    public AdSplashProvide(Activity activity,
                           SplashAdListener adListener) {
        this(activity, adListener, 10000);
    }

    public AdSplashProvide(Activity activity,
                           SplashAdListener adListener, int time_out) {
        this.activity = activity;
        adOpen = AdCustomManager.isAdOpen();
        if (adOpen) {
            loadGroMoreAd(activity, adListener);
        } else {
            loadBeiziAd(activity, adListener);
        }
        loadSplashAd();
        mHandler = new Handler();
        Runnable mTimeOutCheckRunnable = () -> {
            if (activity != null && !activity.isFinishing()) {
                if (adListener != null) {
                    adListener.onAdTimeout();
                }
            }
        };
        mHandler.postDelayed(mTimeOutCheckRunnable, time_out);
    }

    private void loadBeiziAd(Activity activity, SplashAdListener adListener) {
        beiziProvide = new BeiziAdSplashProvide(activity, AdCustomManager.getConfig().getBzSplashAdId(), new AdListener() {
            @Override
            public void onAdLoaded() {
                adListener.onAdLoaded();
            }

            @Override
            public void onAdShown() {
                adListener.onAdExposure();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                adListener.onAdFailed(i, "");
            }

            @Override
            public void onAdClosed() {
                adListener.onAdDismissed();
            }

            @Override
            public void onAdTick(long l) {

            }

            @Override
            public void onAdClicked() {
                adListener.onAdClicked();
            }
        });
    }


    private void loadGroMoreAd(Activity activity,
                               SplashAdListener adListener) {
        groMoreProvide = new GroMoreAdSplashProvide(activity,
                new MediationSplashRequestInfo(MediationConstant.ADN_PANGLE,
                        AdCustomManager.getConfig().getmAdNetworkSlotId(),
                        AdCustomManager
                                .getConfig().getCsjAppId()
                        , "") {
                }, adListener);
    }


    /**
     * 加载开屏广告
     */
    private void loadSplashAd() {
        if (adOpen) {
            if (AdCustomManager.isGroMoreInit()) {
                groMoreProvide.loadSplashAd(AdCustomManager.getConfig().getGroMoreSplashAdId());
            } else {
                //sdk还没加载完成，等初始化完在加载广告
                callback = new AdCustomManager.InitCallback() {
                    @Override
                    public void onSuccess() {
                        groMoreProvide.loadSplashAd(AdCustomManager.getConfig().getGroMoreSplashAdId());
                    }

                    @Override
                    public void onFair() {
                        Log.e("Ad", "广告sdk初始化失败");
                    }
                };
                AdCustomManager.addInitCallback(callback);
            }
        } else {
            beiziProvide.loadAd(UIUtils.getScreenWidthInPx(activity),
                    UIUtils.getScreenHeight(activity));
        }
    }

    public void showAd(ViewGroup contain) {
        if (adOpen) {
            contain.addView(groMoreProvide.getSplashAd().getSplashView());
        } else {
            beiziProvide.showAd(contain);
        }
    }


    /**
     * 在Activity onDestroy中需要调用清理资源
     */
    public void destroy() {
        if (adOpen) {
            groMoreProvide.destroy();
            if (callback != null) {
                AdCustomManager.removeInitCallback(callback);
            }
        } else {
            beiziProvide.destroy();
        }
    }

}
