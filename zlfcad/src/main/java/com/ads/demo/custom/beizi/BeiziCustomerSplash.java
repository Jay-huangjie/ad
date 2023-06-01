package com.ads.demo.custom.beizi;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import com.ads.demo.custom.beizi.util.ThreadUtils;
import com.beizi.fusion.AdListener;
import com.beizi.fusion.SplashAd;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.mediation.MediationConstant;
import com.bytedance.sdk.openadsdk.mediation.bridge.custom.splash.MediationCustomSplashLoader;
import com.bytedance.sdk.openadsdk.mediation.custom.MediationCustomServiceConfig;
import com.zlfcapp.zlfcad.AdCustomConfig;
import com.zlfcapp.zlfcad.AdCustomManager;
import com.zlfcapp.zlfcad.core.AdSplashProvide;
import com.zlfcapp.zlfcad.utils.UIUtils;

/**
 * YLH 开屏广告自定义Adapter
 */
public class BeiziCustomerSplash extends MediationCustomSplashLoader {

    private static final String TAG = BeiziCustomerSplash.class.getSimpleName();
    private SplashAd splashAd;
    private boolean isLoadSuccess;


    @Override
    public void showAd(ViewGroup container) {
        /**
         * 先切子线程，再在子线程中切主线程进行广告展示
         */
        ThreadUtils.runOnUIThreadByThreadPool(new Runnable() {
            @Override
            public void run() {
                if (splashAd != null && container != null) {
                    container.removeAllViews();
                    splashAd.show(container);
                }
            }
        });
    }

    @Override
    public void load(Context context, AdSlot adSlot, MediationCustomServiceConfig mediationCustomServiceConfig) {
        /**
         * 在子线程中进行广告加载
         */
        ThreadUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                splashAd = new SplashAd(context, null, mediationCustomServiceConfig.getADNNetworkSlotId(), new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        isLoadSuccess = true;
                        if (isBidding()) {//bidding类型广告
                            double price = 1500;
                            AdCustomConfig config = AdCustomManager.getConfig();
                            if (config != null) {
                                price = config.getmBindingPrice();
                            }
                            callLoadSuccess(price);  //bidding广告成功回调，回传竞价广告价格
                        } else { //普通类型广告
                            callLoadSuccess();
                        }
                    }

                    @Override
                    public void onAdShown() {
                        callSplashAdShow();
                    }

                    @Override
                    public void onAdFailedToLoad(int errorCode) {
                        isLoadSuccess = false;
                        callLoadFail(errorCode,"Code码详见：http://sdkdoc.beizi.biz/#/zh-cn/android/4xx/ErrorCode");
                    }

                    @Override
                    public void onAdClosed() {
                        callSplashAdDismiss();
                    }

                    @Override
                    public void onAdTick(long millisUnitFinished) {
//                Log.i("BeiZisDemo", "onAdTick millisUnitFinished == " + millisUnitFinished);
                    }

                    @Override
                    public void onAdClicked() {
                        callSplashAdClicked();
                    }
                }, AdSplashProvide.TIME_OUT);
                splashAd.loadAd(UIUtils.getScreenWidthInPx(context),
                        UIUtils.getScreenHeight(context));
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        splashAd = null;
    }

    /**
     * 是否是Bidding广告
     *
     * @return
     */
    public boolean isBidding() {
        return getBiddingType() == MediationConstant.AD_TYPE_CLIENT_BIDING;
    }
}
