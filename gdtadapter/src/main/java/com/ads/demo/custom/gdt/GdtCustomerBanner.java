package com.ads.demo.custom.gdt;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;

import com.ads.demo.AppConst;
import com.ads.demo.custom.Const;
import com.ads.demo.util.ThreadUtils;
import com.bytedance.msdk.api.v2.GMAdConstant;
import com.bytedance.msdk.api.v2.ad.custom.GMCustomAdError;
import com.bytedance.msdk.api.v2.ad.custom.banner.GMCustomBannerAdapter;
import com.bytedance.msdk.api.v2.ad.custom.bean.GMCustomServiceConfig;
import com.bytedance.msdk.api.v2.slot.GMAdSlotBanner;
import com.qq.e.ads.banner2.UnifiedBannerADListener;
import com.qq.e.ads.banner2.UnifiedBannerView;
import com.qq.e.comm.util.AdError;

/**
 * YLH Banner自定义Adapter
 */
public class GdtCustomerBanner extends GMCustomBannerAdapter {

    private static final String TAG = AppConst.TAG_PRE + GdtCustomerBanner.class.getSimpleName();

    private UnifiedBannerView mUnifiedBannerView;

    @Override
    public void load(Context context, GMAdSlotBanner adSlot, GMCustomServiceConfig serviceConfig) {
        /**
         * 在子线程中进行广告加载
         */
        ThreadUtils.runOnThreadPool(new Runnable() {
            @Override
            public void run() {
                if (context instanceof Activity) {
                    mUnifiedBannerView = new UnifiedBannerView((Activity) context, serviceConfig.getADNNetworkSlotId(),
                            new UnifiedBannerADListener() {
                                @Override
                                public void onNoAD(AdError adError) {
                                    if (adError != null) {
                                        Log.i(TAG, "onNoAD errorCode = " + adError.getErrorCode() + " errorMessage = " + adError.getErrorMsg());
                                        callLoadFail(new GMCustomAdError(adError.getErrorCode(), adError.getErrorMsg()));
                                    } else {
                                        callLoadFail(new GMCustomAdError(Const.LOAD_ERROR, "no ad"));
                                    }
                                }

                                @Override
                                public void onADReceive() {
                                    Log.i(TAG, "onADReceive");
                                    if (isBidding()) {//bidding类型广告
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
                                    callBannerAdClicked();
                                }

                                @Override
                                public void onADLeftApplication() {
                                    Log.i(TAG, "onADLeftApplication");
                                    callBannerAdLeftApplication();
                                }
                            });
                    mUnifiedBannerView.setRefresh(0); // 设置0表示不轮播，m统一处理了轮播无需设置
                    mUnifiedBannerView.loadAD();
                } else {
                    callLoadFail(new GMCustomAdError(Const.LOAD_ERROR, "context is not Activity"));
                }
            }
        });
    }

    @Override
    public View getAdView() {
        return mUnifiedBannerView;
    }

    @Override
    public GMAdConstant.AdIsReadyStatus isReadyStatus() {
        if (mUnifiedBannerView != null && mUnifiedBannerView.isValid()) {
            return GMAdConstant.AdIsReadyStatus.AD_IS_READY;
        } else {
            return GMAdConstant.AdIsReadyStatus.AD_IS_NOT_READY;
        }
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
    public boolean isBidding() {
        return getBiddingType() == GMAdConstant.AD_TYPE_CLIENT_BIDING;
    }
}
