package com.ads.demo.custom.beizi;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import com.ads.demo.util.ThreadUtils;
import com.beizi.fusion.AdListener;
import com.beizi.fusion.SplashAd;
import com.bytedance.msdk.api.v2.GMAdConstant;
import com.bytedance.msdk.api.v2.ad.custom.GMCustomAdError;
import com.bytedance.msdk.api.v2.ad.custom.bean.GMCustomServiceConfig;
import com.bytedance.msdk.api.v2.ad.custom.splash.GMCustomSplashAdapter;
import com.bytedance.msdk.api.v2.slot.GMAdSlotSplash;
import com.zlfcapp.zlfcad.utils.UIUtils;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * YLH 开屏广告自定义Adapter
 */
public class BeiziCustomerSplash extends GMCustomSplashAdapter {

    private static final String TAG = BeiziCustomerSplash.class.getSimpleName();
    private SplashAd splashAd;
    private boolean isLoadSuccess;

    @Override
    public void load(Context context, GMAdSlotSplash adSlot, GMCustomServiceConfig serviceConfig) {
        /**
         * 在子线程中进行广告加载
         */
        ThreadUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                splashAd = new SplashAd(context, null, serviceConfig.getADNNetworkSlotId(), new AdListener() {
                    @Override
                    public void onAdLoaded() {
                        isLoadSuccess = true;
                        if (isBidding()) {//bidding类型广告
                            callLoadSuccess(1500);  //bidding广告成功回调，回传竞价广告价格
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
                        callLoadFail(new GMCustomAdError(errorCode, "Code码详见：http://sdkdoc.beizi.biz/#/zh-cn/android/4xx/ErrorCode"));
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
                }, 1500);
                splashAd.loadAd(UIUtils.getScreenWidthInPx(context),
                        UIUtils.getScreenHeight(context));
            }
        });

    }

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


    @Override
    public GMAdConstant.AdIsReadyStatus isReadyStatus() {
        Log.e(TAG, "isReadyStatus");
        /**
         * 在子线程中进行广告是否可用的判断
         */
        Future<GMAdConstant.AdIsReadyStatus> future = ThreadUtils.runOnThreadPool(new Callable<GMAdConstant.AdIsReadyStatus>() {
            @Override
            public GMAdConstant.AdIsReadyStatus call() throws Exception {
                if (splashAd != null && isLoadSuccess) {
                    return GMAdConstant.AdIsReadyStatus.AD_IS_READY;
                } else {
                    return GMAdConstant.AdIsReadyStatus.AD_IS_NOT_READY;
                }
            }
        });
        try {
            GMAdConstant.AdIsReadyStatus result = future.get(500, TimeUnit.MILLISECONDS);//设置500毫秒的总超时，避免线程阻塞
            if (result != null) {
                return result;
            } else {
                return GMAdConstant.AdIsReadyStatus.AD_IS_NOT_READY;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return GMAdConstant.AdIsReadyStatus.AD_IS_NOT_READY;
    }

    /**
     * 是否是Bidding广告
     *
     * @return
     */
    public boolean isBidding() {
        return getBiddingType() == GMAdConstant.AD_TYPE_CLIENT_BIDING;
    }
}
