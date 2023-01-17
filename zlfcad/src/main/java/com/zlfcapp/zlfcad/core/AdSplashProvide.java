package com.zlfcapp.zlfcad.core;

import android.app.Activity;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.beizi.fusion.AdListener;
import com.bytedance.msdk.adapter.pangle.PangleNetworkRequestInfo;
import com.bytedance.msdk.api.AdError;
import com.bytedance.msdk.api.v2.ad.splash.GMSplashAdListener;
import com.bytedance.msdk.api.v2.ad.splash.GMSplashAdLoadCallback;
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

    public AdSplashProvide(Activity activity,
                           SplashAdListener adListener) {
        this.activity = activity;
        adOpen = AdCustomManager.isAdOpen();
        if (adOpen) {
            loadGroMoreAd(activity,adListener);
        } else {
            loadBeiziAd(activity, adListener);
        }
        loadSplashAd();
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
                new PangleNetworkRequestInfo(AdCustomManager
                        .getConfig().getGroMoreAppId(),
                        AdCustomManager.getConfig().getmAdNetworkSlotId()), new GMSplashAdLoadCallback() {
            @Override
            public void onAdLoadTimeout() {

            }

            @Override
            public void onSplashAdLoadFail(@NonNull AdError adError) {
                adListener.onAdFailed(adError.code, adError.message);
            }

            @Override
            public void onSplashAdLoadSuccess() {
//                groMoreProvide.printInfo();
                adListener.onAdLoaded();
            }
        }, new GMSplashAdListener() {
            @Override
            public void onAdClicked() {
                adListener.onAdClicked();
            }

            @Override
            public void onAdDismiss() {
                adListener.onAdDismissed();
            }

            @Override
            public void onAdShow() {
                adListener.onAdExposure();
            }

            @Override
            public void onAdShowFail(@NonNull AdError adError) {
                if (groMoreProvide != null) {
                    groMoreProvide.loadSplashAd(AdCustomManager
                            .getConfig().getGroMoreSplashAdId());
                }
            }

            @Override
            public void onAdSkip() {
                adListener.onAdDismissed();
            }
        });
    }


    /**
     * 加载开屏广告
     */
    private void loadSplashAd() {
        if (adOpen) {
            groMoreProvide.loadSplashAd(AdCustomManager.getConfig().getGroMoreSplashAdId());
        } else {
            beiziProvide.loadAd(UIUtils.getScreenWidthInPx(activity),
                    UIUtils.getScreenHeight(activity));
        }
    }

    public void showAd(ViewGroup contain) {
        if (adOpen) {
            groMoreProvide.getSplashAd().showAd(contain);
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
        } else {
            beiziProvide.destroy();
        }
    }

}
