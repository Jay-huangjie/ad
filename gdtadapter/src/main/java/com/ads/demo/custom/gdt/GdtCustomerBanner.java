package com.ads.demo.custom.gdt;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.ads.demo.util.ThreadUtils;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.mediation.MediationConstant;
import com.bytedance.sdk.openadsdk.mediation.bridge.custom.banner.MediationCustomBannerLoader;
import com.bytedance.sdk.openadsdk.mediation.custom.MediationCustomServiceConfig;
import com.qq.e.ads.banner2.UnifiedBannerADListener;
import com.qq.e.ads.banner2.UnifiedBannerView;
import com.qq.e.comm.util.AdError;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * YLH Banner自定义Adapter
 */
public class GdtCustomerBanner extends MediationCustomBannerLoader {

    private static final String TAG = GdtCustomerBanner.class.getSimpleName();

    private UnifiedBannerView mUnifiedBannerView;


    @Override
    public void load(Context context, AdSlot adSlot, MediationCustomServiceConfig mediationCustomServiceConfig) {
        /**
         * 在子线程中进行广告加载
         */
        ThreadUtils.runOnThreadPool(new Runnable() {
            @Override
            public void run() {
                if (context instanceof Activity) {
                    mUnifiedBannerView = new UnifiedBannerView((Activity) context, mediationCustomServiceConfig.getADNNetworkSlotId(),
                            new UnifiedBannerADListener() {
                                @Override
                                public void onNoAD(AdError adError) {
                                    if (adError != null) {
                                        Log.i(TAG, "onNoAD errorCode = " + adError.getErrorCode() + " errorMessage = " + adError.getErrorMsg());
                                        callLoadFail(adError.getErrorCode(), adError.getErrorMsg());
                                    } else {
                                        callLoadFail(99999, "no ad");
                                    }
                                }

                                @Override
                                public void onADReceive() {
                                    Log.i(TAG, "onADReceive");
                                    if (isClientBidding()) {//bidding类型广告
                                        double ecpm = mUnifiedBannerView.getECPM();//当无权限调用该接口时，SDK会返回错误码-1
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
                                public void onADExposure() {
                                    Log.i(TAG, "onADExposure");
                                    callBannerAdShow();
                                }

                                @Override
                                public void onADClosed() {
                                    Log.i(TAG, "onADClosed");
                                    callBannerAdClosed();
                                }

                                @Override
                                public void onADClicked() {
                                    Log.i(TAG, "onADClicked");
                                    callBannerAdClick();
                                }

                                @Override
                                public void onADLeftApplication() {
                                    Log.i(TAG, "onADLeftApplication");
                                }
                            });
                    mUnifiedBannerView.setRefresh(0); // 设置0表示不轮播，m统一处理了轮播无需设置
                    mUnifiedBannerView.loadAD();
                } else {
                    callLoadFail(40000, "context is not Activity");
                }
            }
        });
    }

    @Override
    public View getAdView() {
        return mUnifiedBannerView;
    }

    @Override
    public MediationConstant.AdIsReadyStatus isReadyCondition() {
        /**
         * 在子线程中进行广告是否可用的判断
         */
        Future<MediationConstant.AdIsReadyStatus> future = ThreadUtils.runOnThreadPool(new Callable<MediationConstant.AdIsReadyStatus>() {
            @Override
            public MediationConstant.AdIsReadyStatus call() throws Exception {
                if (mUnifiedBannerView != null && mUnifiedBannerView.isValid()) {
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
        ThreadUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                if (mUnifiedBannerView != null) {
                    mUnifiedBannerView.destroy();
                    mUnifiedBannerView = null;
                }
            }
        });
    }

    /**
     * 是否是Bidding广告
     *
     * @return
     */
    public boolean isClientBidding() {
        return getBiddingType() == MediationConstant.AD_TYPE_CLIENT_BIDING;
    }
}
