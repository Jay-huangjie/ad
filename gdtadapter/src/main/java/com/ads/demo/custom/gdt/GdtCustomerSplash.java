package com.ads.demo.custom.gdt;

import android.content.Context;
import android.os.SystemClock;
import android.util.Log;
import android.view.ViewGroup;

import com.ads.demo.util.ThreadUtils;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.mediation.MediationConstant;
import com.bytedance.sdk.openadsdk.mediation.bridge.custom.splash.MediationCustomSplashLoader;
import com.bytedance.sdk.openadsdk.mediation.custom.MediationCustomServiceConfig;
import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;
import com.qq.e.comm.util.AdError;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * YLH 开屏广告自定义Adapter
 */
public class GdtCustomerSplash extends MediationCustomSplashLoader {

    private static final String TAG = "TTMediationSDK_" + GdtCustomerSplash.class.getSimpleName();
    private volatile SplashAD mSplashAD;
    private boolean isLoadSuccess;


    @Override
    public void load(Context context, AdSlot adSlot, MediationCustomServiceConfig serviceConfig) {
        /**
         * 在子线程中进行广告加载
         */
        Log.e(TAG, "load gdt custom splash ad-----");
        ThreadUtils.runOnThreadPool(new Runnable() {
            @Override
            public void run() {
                SplashADListener splashADListener = new SplashADListener() {
                    @Override
                    public void onADDismissed() {
                        Log.i(TAG, "onADDismissed");
                        callSplashAdDismiss();
                    }

                    @Override
                    public void onNoAD(AdError adError) {
                        isLoadSuccess = false;
                        if (adError != null) {
                            Log.i(TAG, "onNoAD errorCode = " + adError.getErrorCode() + " errorMessage = " + adError.getErrorMsg());
                            callLoadFail(adError.getErrorCode(), adError.getErrorMsg());
                        } else {
                            callLoadFail(40000, "no ad");
                        }
                    }

                    @Override
                    public void onADPresent() {
                        Log.i(TAG, "onADPresent");
                    }

                    @Override
                    public void onADClicked() {
                        Log.i(TAG, "onADClicked");
                        callSplashAdClicked();
                    }

                    @Override
                    public void onADTick(long l) {
                        Log.i(TAG, "onADTick");
                    }

                    @Override
                    public void onADExposure() {
                        Log.i(TAG, "onADExposure");
                        callSplashAdShow();
                    }

                    @Override
                    public void onADLoaded(long expireTimestamp) {
                        Log.i(TAG, "onADLoaded");
                        long timeIntervalSec = expireTimestamp - SystemClock.elapsedRealtime();
                        if (timeIntervalSec > 1000) {
                            isLoadSuccess = true;
                            if (isClientBidding()) {//bidding类型广告
                                double ecpm = mSplashAD.getECPM(); //当无权限调用该接口时，SDK会返回错误码-1
                                if (ecpm < 0) {
                                    ecpm = 0;
                                }
                                Log.e(TAG, "ecpm:" + ecpm);
                                callLoadSuccess(ecpm);  //bidding广告成功回调，回传竞价广告价格
                            } else { //普通类型广告
                                callLoadSuccess();
                            }
                        } else {
                            isLoadSuccess = false;
                            callLoadFail(40000, "ad has expired");
                        }
                    }
                };
                if (isServerBidding()) {
                    mSplashAD = new SplashAD(context, serviceConfig.getADNNetworkSlotId(), splashADListener, 3000, getAdm());
                } else {
                    mSplashAD = new SplashAD(context, serviceConfig.getADNNetworkSlotId(), splashADListener, 3000);
                }
                mSplashAD.fetchAdOnly();
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
                if (mSplashAD != null && container != null) {
                    container.removeAllViews();
                    if (isServerBidding()) {
                        mSplashAD.setBidECPM(mSplashAD.getECPM());
                    }
                    mSplashAD.showAd(container);
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
        mSplashAD = null;
    }

    @Override
    public MediationConstant.AdIsReadyStatus isReadyCondition() {

        /**
         * 在子线程中进行广告是否可用的判断
         */
        Future<MediationConstant.AdIsReadyStatus> future = ThreadUtils.runOnThreadPool(new Callable<MediationConstant.AdIsReadyStatus>() {
            @Override
            public MediationConstant.AdIsReadyStatus call() throws Exception {
                if (mSplashAD != null && mSplashAD.isValid()) {
                    return MediationConstant.AdIsReadyStatus.AD_IS_READY;
                } else {
                    return MediationConstant.AdIsReadyStatus.AD_IS_NOT_READY;
                }
            }
        });
        try {
            MediationConstant.AdIsReadyStatus result = future.get(500, TimeUnit.MILLISECONDS);//设置500毫秒的总超时，避免线程阻塞
            if (result != null) {
                return result;
            } else {
                return MediationConstant.AdIsReadyStatus.AD_IS_NOT_READY;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MediationConstant.AdIsReadyStatus.AD_IS_NOT_READY;
    }

    /**
     * 是否clientBidding广告
     *
     * @return
     */
    public boolean isClientBidding() {
        return getBiddingType() == MediationConstant.AD_TYPE_CLIENT_BIDING;
    }

    /**
     * 是否serverBidding广告
     *
     * @return
     */
    public boolean isServerBidding() {
        return getBiddingType() == MediationConstant.AD_TYPE_SERVER_BIDING;
    }
}
