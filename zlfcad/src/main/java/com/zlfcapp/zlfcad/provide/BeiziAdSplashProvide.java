package com.zlfcapp.zlfcad.provide;

import android.app.Activity;
import android.view.ViewGroup;

import com.beizi.fusion.AdListener;
import com.beizi.fusion.SplashAd;
import com.zlfcapp.zlfcad.core.AdSplashProvide;

/**
 * create by hj on 2023/1/6
 **/
public class BeiziAdSplashProvide {
    private SplashAd splashAd;
    private Activity activity;

    public BeiziAdSplashProvide(Activity activity,
                                String adId, AdListener listener) {
        this.activity = activity;
        splashAd = new SplashAd(activity, null, adId, listener,5000);
    }

    public void loadAd(int width, int height) {
        splashAd.loadAd(width, height);
    }

    public void showAd(ViewGroup container) {
        if (splashAd != null) {
            splashAd.show(container);
        }
    }

    public void destroy() {
        if (splashAd != null) {
            splashAd.cancel(activity);
        }
        activity = null;
        splashAd = null;
    }
}
