package com.ads.demo.custom.beizi;


import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.ads.demo.custom.beizi.util.ThreadUtils;
import com.ads.demo.custom.beizi.util.UIUtil;
import com.beizi.fusion.AdListener;
import com.beizi.fusion.SplashAd;
import com.bytedance.pangle.wrapper.PluginActivityWrapper;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.mediation.MediationConstant;
import com.bytedance.sdk.openadsdk.mediation.bridge.custom.splash.MediationCustomSplashLoader;
import com.bytedance.sdk.openadsdk.mediation.custom.MediationCustomServiceConfig;

import org.json.JSONObject;

/**
 * BeiZi适配器自定义开屏广告
 */
public class BeiZiCustomSplashLoader extends MediationCustomSplashLoader {

    private SplashAd mSplashAd;
    private long timeout = 5000;//默认是5000

    @Override
    public void load(Context context, AdSlot adSlot,
                     MediationCustomServiceConfig mediationCustomServiceConfig) {
        try {
            if (adSlot == null || mediationCustomServiceConfig == null) {
                callLoadFail(1001, "adSlot or  mediationCustomServiceConfig param error");
                return;
            }
            String spaceId = mediationCustomServiceConfig.getADNNetworkSlotId();
            if (TextUtils.isEmpty(spaceId)) {
                callLoadFail(1002, "spaceId is empty");
                return;
            }
            String customAdapterJson = mediationCustomServiceConfig.getCustomAdapterJson();
            if (!TextUtils.isEmpty(customAdapterJson)) {
                JSONObject jsonObject = new JSONObject(customAdapterJson);
                if (jsonObject.has("timeout") && 0 != (jsonObject.getLong("timeout"))) {
                    timeout = jsonObject.getLong("timeout");
                }
            }
            int width = UIUtil.px2dip(context, adSlot.getImgAcceptedWidth());
            int height = UIUtil.px2dip(context, adSlot.getImgAcceptedHeight());
            ThreadUtils.runOnUIThreadByThreadPool(new Runnable() {
                @Override
                public void run() {
                    if (mSplashAd == null) {
                        Context activityContext = context;
                        if (context instanceof PluginActivityWrapper) {
                            activityContext = ((PluginActivityWrapper) context).mOriginActivity;
                        }
                        mSplashAd = new SplashAd(activityContext, null, spaceId, new AdListener() {
                            @Override
                            public void onAdLoaded() {
                                if (isClientBidding()) {
                                    double ecpm = mSplashAd.getECPM();
                                    if (ecpm < 0) {
                                        ecpm = 0;
                                    }
                                    callLoadSuccess(ecpm);
                                } else {
                                    callLoadSuccess();
                                }
                            }

                            @Override
                            public void onAdShown() {
                                callSplashAdShow();
                            }

                            @Override
                            public void onAdFailedToLoad(int i) {
                                callLoadFail(i, i + "");
                            }

                            @Override
                            public void onAdClosed() {
                                callSplashAdDismiss();
                            }

                            @Override
                            public void onAdTick(long l) {

                            }

                            @Override
                            public void onAdClicked() {
                                callSplashAdClicked();
                            }
                        }, timeout);
                    }
                    mSplashAd.loadAd(width, height);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            callLoadFail(1003, "unknown error :" + e.getMessage());
        }
    }

    @Override
    public void showAd(ViewGroup viewGroup) {
        ThreadUtils.runOnUIThreadByThreadPool(new Runnable() {
            @Override
            public void run() {
                if (mSplashAd != null) {
                    mSplashAd.show(viewGroup);
                }
            }
        });
    }

    /**
     * 是否clientBidding广告
     *
     * @return true 是 false 不是
     */
    public boolean isClientBidding() {
        return getBiddingType() == MediationConstant.AD_TYPE_CLIENT_BIDING;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSplashAd != null) {
            mSplashAd.cancel(null);
        }
        mSplashAd = null;
    }
}
