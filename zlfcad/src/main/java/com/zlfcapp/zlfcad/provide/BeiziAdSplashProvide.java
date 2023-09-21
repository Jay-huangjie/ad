package com.zlfcapp.zlfcad.provide;

import android.app.Activity;
import android.view.ViewGroup;

import com.beizi.fusion.AdListener;
import com.beizi.fusion.SplashAd;
import com.zlfcapp.zlfcad.core.AdSplashProvide;
import com.zlfcapp.zlfcad.core.BaseProvide;
import com.zlfcapp.zlfcad.listener.SplashAdListener;
import com.zlfcapp.zlfcad.utils.UIUtils;

/**
 * create by hj on 2023/1/6
 **/
public class BeiziAdSplashProvide extends BaseProvide {
    private SplashAd splashAd;

    public BeiziAdSplashProvide(Activity activity,
                                String adId, SplashAdListener adListener) {
        super(activity);
        splashAd = new SplashAd(activity, null, adId, new AdListener() {
            @Override
            public void onAdLoaded() {
                adListener.onAdLoaded();
            }

            @Override
            public void onAdShown() {
                setAdShow(true);
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
        }, 5000);
    }


    @Override
    public void showAd(ViewGroup container) {
        if (splashAd != null) {
            splashAd.show(container);
        }
    }

    @Override
    public void loadAd() {
        splashAd.loadAd(UIUtils.getScreenWidthInPx(mActivity),
                UIUtils.getScreenHeight(mActivity));
    }

    @Override
    public void destroy() {
        if (splashAd != null) {
            splashAd.cancel(mActivity);
        }
        mActivity = null;
        splashAd = null;
    }
}
