package com.ads.demo.custom.gdt;

import android.content.Context;
import android.util.Log;

import com.ads.demo.util.ThreadUtils;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.mediation.MediationConstant;
import com.bytedance.sdk.openadsdk.mediation.bridge.custom.draw.MediationCustomDrawLoader;
import com.bytedance.sdk.openadsdk.mediation.custom.MediationCustomServiceConfig;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.nativ.NativeADUnifiedListener;
import com.qq.e.ads.nativ.NativeUnifiedAD;
import com.qq.e.ads.nativ.NativeUnifiedADData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhy on date
 * Usage:
 * Doc:
 */
public class GdtCustomerDraw extends MediationCustomDrawLoader {

    private static final String TAG = GdtCustomerDraw.class.getSimpleName();
    private VideoOption videoOption;
    private volatile NativeUnifiedAD nativeUnifiedAD;

    @Override
    public void load(Context context, AdSlot adSlot, MediationCustomServiceConfig serviceConfig) {
        /**
         * 在子线程中进行广告加载
         */
        ThreadUtils.runOnThreadPool(new Runnable() {
            @Override
            public void run() {
                NativeADUnifiedListener nativeADUnifiedListener = new NativeADUnifiedListener() {
                    @Override
                    public void onADLoaded(List<NativeUnifiedADData> list) {
                        if (list != null && list.size() > 0) {
                            List<GdtDrawAd> adList = new ArrayList<>();
                            for (NativeUnifiedADData adData : list) {
                                adList.add(new GdtDrawAd(adData, videoOption));
                            }
                            callLoadSuccess(adList);
                        } else {
                            callLoadFail(40000, "no ad");
                        }
                    }

                    @Override
                    public void onNoAD(com.qq.e.comm.util.AdError adError) {
                        if (adError != null) {
                            Log.i(TAG, "onNoAD errorCode = " + adError.getErrorCode() + " errorMessage = " + adError.getErrorMsg());
                            callLoadFail(adError.getErrorCode(), adError.getErrorMsg());
                        } else {
                            callLoadFail(40000, "no ad");
                        }
                    }
                };

                if (isServerBidding()) {
                    nativeUnifiedAD = new NativeUnifiedAD(context, serviceConfig.getADNNetworkSlotId(), nativeADUnifiedListener, getAdm());
                } else {
                    nativeUnifiedAD = new NativeUnifiedAD(context, serviceConfig.getADNNetworkSlotId(), nativeADUnifiedListener);
                }

                nativeUnifiedAD.loadData(1);
            }
        });
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

    @Override
    public void receiveBidResult(boolean win, double winnerPrice, int loseReason, Map<String, Object> extra) {
        super.receiveBidResult(win, winnerPrice, loseReason, extra);
    }
}
