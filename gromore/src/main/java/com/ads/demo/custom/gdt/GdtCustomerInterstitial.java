package com.ads.demo.custom.gdt;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.ads.demo.AppConst;
import com.ads.demo.custom.Const;
import com.ads.demo.util.ThreadUtils;
import com.bytedance.msdk.api.v2.GMAdConstant;
import com.bytedance.msdk.api.v2.ad.custom.GMCustomAdError;
import com.bytedance.msdk.api.v2.ad.custom.bean.GMCustomServiceConfig;
import com.bytedance.msdk.api.v2.ad.custom.interstitial.GMCustomInterstitialAdapter;
import com.bytedance.msdk.api.v2.slot.GMAdSlotInterstitial;
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD;
import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener;
import com.qq.e.comm.pi.IBidding;
import com.qq.e.comm.util.AdError;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * YLH 插屏广告自定义Adapter
 */
public class GdtCustomerInterstitial extends GMCustomInterstitialAdapter {

    private static final String TAG = AppConst.TAG_PRE + GdtCustomerInterstitial.class.getSimpleName();

    private volatile UnifiedInterstitialAD mUnifiedInterstitialAD;
    private boolean isLoadSuccess;

    private GMCustomServiceConfig customServiceConfig;

    @Override
    public void receiveBidResult(boolean win, double winnerPrice, int loseReason, Map<String, Object> map) {
        if (mUnifiedInterstitialAD != null) {
            Map<String, Object> reasonMap = new HashMap<>();
            if (win) {
                reasonMap.put(IBidding.EXPECT_COST_PRICE, Integer.parseInt(String.valueOf(winnerPrice)));
                mUnifiedInterstitialAD.sendWinNotification(reasonMap);
            } else {
                reasonMap.put(IBidding.WIN_PRICE, Integer.parseInt(String.valueOf(winnerPrice)));
                reasonMap.put(IBidding.LOSS_REASON, loseReason);
                if (customServiceConfig != null) {
                    reasonMap.put(IBidding.ADN_ID, customServiceConfig.getADNNetworkSlotId());
                }
                mUnifiedInterstitialAD.sendLossNotification(reasonMap);
            }
        }
        super.receiveBidResult(win, winnerPrice, loseReason, map);
    }

    @Override
    public void load(Context context, GMAdSlotInterstitial adSlot, GMCustomServiceConfig serviceConfig) {
        customServiceConfig = serviceConfig;
        /**
         * 在子线程中进行广告加载
         */
        ThreadUtils.runOnThreadPool(new Runnable() {
            @Override
            public void run() {
                if (context instanceof Activity) {
                    mUnifiedInterstitialAD = new UnifiedInterstitialAD((Activity) context, serviceConfig.getADNNetworkSlotId(),
                            new UnifiedInterstitialADListener() {
                                @Override
                                public void onADReceive() {
                                    isLoadSuccess = true;
                                    Log.i(TAG, "onADReceive");
                                    if (isBidding()) { //bidding类型广告
                                        double ecpm = mUnifiedInterstitialAD.getECPM();//当无权限调用该接口时，SDK会返回错误码-1
                                        if (ecpm < 0) {
                                            ecpm = 0;
                                        }
                                        Log.e(TAG, "ecpm:" + ecpm);
                                        callLoadSuccess(ecpm);
                                    } else {//普通类型广告
                                        callLoadSuccess();
                                    }
                                }

                                @Override
                                public void onVideoCached() {
                                    Log.i(TAG, "onVideoCached");
                                }

                                @Override
                                public void onNoAD(AdError adError) {
                                    isLoadSuccess = false;
                                    if (adError != null) {
                                        Log.i(TAG, "onNoAD errorCode = " + adError.getErrorCode() + " errorMessage = " + adError.getErrorMsg());
                                        callLoadFail(new GMCustomAdError(adError.getErrorCode(), adError.getErrorMsg()));
                                    } else {
                                        callLoadFail(new GMCustomAdError(Const.LOAD_ERROR, "no ad"));
                                    }
                                }

                                @Override
                                public void onADOpened() {
                                    Log.i(TAG, "onADOpened");
                                    callInterstitialAdOpened();
                                }

                                @Override
                                public void onADExposure() {
                                    Log.i(TAG, "onADExposure");
                                    callInterstitialShow();
                                }

                                @Override
                                public void onADClicked() {
                                    Log.i(TAG, "onADClicked");
                                    callInterstitialAdClick();
                                }

                                @Override
                                public void onADLeftApplication() {
                                    Log.i(TAG, "onADLeftApplication");
                                    callInterstitialAdLeftApplication();
                                }

                                @Override
                                public void onADClosed() {
                                    Log.i(TAG, "onADClosed");
                                    callInterstitialClosed();
                                }

                                @Override
                                public void onRenderSuccess() {

                                }

                                @Override
                                public void onRenderFail() {

                                }
                            });
                    mUnifiedInterstitialAD.loadAD();
                } else {
                    callLoadFail(new GMCustomAdError(Const.LOAD_ERROR, "context is not Activity"));
                }

            }
        });
    }

    @Override
    public void showAd(Activity activity) {
        Log.i(TAG, "自定义的showAd");
        /**
         * 先切子线程，再在子线程中切主线程进行广告展示
         */
        ThreadUtils.runOnUIThreadByThreadPool(new Runnable() {
            @Override
            public void run() {
                if (mUnifiedInterstitialAD != null) {
                    mUnifiedInterstitialAD.show(activity);
                }
            }
        });

    }

    @Override
    public GMAdConstant.AdIsReadyStatus isReadyStatus() {
        /**
         * 在子线程中进行广告是否可用的判断
         */
        Future<GMAdConstant.AdIsReadyStatus> future = ThreadUtils.runOnThreadPool(new Callable<GMAdConstant.AdIsReadyStatus>() {
            @Override
            public GMAdConstant.AdIsReadyStatus call() throws Exception {
                if (mUnifiedInterstitialAD != null && mUnifiedInterstitialAD.isValid()) {
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

        /**
         * 在子线程中进行广告销毁
         */
        ThreadUtils.runOnThreadPool(new Runnable() {
            @Override
            public void run() {
                if (mUnifiedInterstitialAD != null) {
                    mUnifiedInterstitialAD.destroy();
                    mUnifiedInterstitialAD = null;
                }
            }
        });
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
