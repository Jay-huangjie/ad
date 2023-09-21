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
    private final boolean adOpen;
    private Handler mHandler;

    private final BaseProvide mProvide;

    private AdCustomManager.InitCallback callback;

    public AdSplashProvide(Activity activity,
                           SplashAdListener adListener) {
        this(activity, adListener, 10000);
    }

    public AdSplashProvide(Activity activity,
                           SplashAdListener adListener, int time_out) {
        adOpen = AdCustomManager.isAdOpen();
        if (adOpen) {
            mProvide = new GroMoreAdSplashProvide(activity,
                    new MediationSplashRequestInfo(MediationConstant.ADN_PANGLE,
                            AdCustomManager.getConfig().getmAdNetworkSlotId(),
                            AdCustomManager
                                    .getConfig().getCsjAppId()
                            , "") {
                    }, adListener);
        } else {
            mProvide = new BeiziAdSplashProvide(activity,
                    AdCustomManager.getConfig().getBzSplashAdId(), adListener);
        }
        loadSplashAd();
        mHandler = new Handler();
        Runnable mTimeOutCheckRunnable = () -> {
            if (mProvide.isAdShow()) {
                return;
            }
            if (activity != null && !activity.isFinishing()) {
                if (adListener != null) {
                    adListener.onAdTimeout();
                }
            }
        };
        mHandler.postDelayed(mTimeOutCheckRunnable, time_out);
    }


    /**
     * 加载开屏广告
     */
    private void loadSplashAd() {
        if (adOpen) {
            if (AdCustomManager.isGroMoreInit()) {
                mProvide.loadAd();
            } else {
                //sdk还没加载完成，等初始化完在加载广告
                callback = new AdCustomManager.InitCallback() {
                    @Override
                    public void onSuccess() {
                        mProvide.loadAd();
                    }

                    @Override
                    public void onFair() {
                        Log.e("Ad", "广告sdk初始化失败");
                    }
                };
                AdCustomManager.addInitCallback(callback);
            }
        } else {
            mProvide.loadAd();
        }
    }

    public void showAd(ViewGroup contain) {
        mProvide.showAd(contain);
    }


    /**
     * 在Activity onDestroy中需要调用清理资源
     */
    public void destroy() {
        mProvide.destroy();
        if (callback != null) {
            AdCustomManager.removeInitCallback(callback);
        }
        mHandler.removeCallbacksAndMessages(null);
    }

}
