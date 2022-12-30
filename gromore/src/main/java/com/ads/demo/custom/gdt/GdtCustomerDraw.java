package com.ads.demo.custom.gdt;

import android.content.Context;
import android.util.Log;

import com.ads.demo.AppConst;
import com.ads.demo.custom.Const;
import com.ads.demo.util.ThreadUtils;
import com.bytedance.msdk.adapter.gdt.GDTAdapterUtils;
import com.bytedance.msdk.api.GDTExtraOption;
import com.bytedance.msdk.api.v2.GMAdConstant;
import com.bytedance.msdk.api.v2.ad.custom.GMCustomAdError;
import com.bytedance.msdk.api.v2.ad.custom.bean.GMCustomServiceConfig;
import com.bytedance.msdk.api.v2.ad.custom.draw.GMCustomDrawAdapter;
import com.bytedance.msdk.api.v2.slot.GMAdSlotDraw;
import com.bytedance.msdk.api.v2.slot.paltform.GMAdSlotGDTOption;
import com.qq.e.ads.cfg.DownAPPConfirmPolicy;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.nativ.NativeADUnifiedListener;
import com.qq.e.ads.nativ.NativeUnifiedAD;
import com.qq.e.ads.nativ.NativeUnifiedADData;
import com.qq.e.comm.util.AdError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhy on date
 * Usage:
 * Doc:
 */
public class GdtCustomerDraw extends GMCustomDrawAdapter {

    private static final String TAG = AppConst.TAG_PRE + GdtCustomerDraw.class.getSimpleName();
    private VideoOption videoOption;
    private volatile NativeUnifiedAD nativeUnifiedAD;

    @Override
    public void load(Context context, GMAdSlotDraw adSlot, GMCustomServiceConfig serviceConfig) {
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
                                adList.add(new GdtDrawAd(adData, GdtCustomerDraw.this,videoOption));
                            }
                            callLoadSuccess(adList);
                        } else {
                            callLoadFail(new GMCustomAdError(Const.LOAD_ERROR, "no ad"));
                        }
                    }

                    @Override
                    public void onNoAD(AdError adError) {
                        if (adError != null) {
                            Log.i(TAG, "onNoAD errorCode = " + adError.getErrorCode() + " errorMessage = " + adError.getErrorMsg());
                            callLoadFail(new GMCustomAdError(adError.getErrorCode(), adError.getErrorMsg()));
                        } else {
                            callLoadFail(new GMCustomAdError(Const.LOAD_ERROR, "no ad"));
                        }
                    }
                };

                nativeUnifiedAD = new NativeUnifiedAD(context, serviceConfig.getADNNetworkSlotId(), nativeADUnifiedListener);

                int maxVideoDuration = 0;
                int minVideoDuration = 0;
                GMAdSlotGDTOption option = adSlot.getGMAdSlotGDTOption();
                if (option != null) {
                    maxVideoDuration = option.getGDTMaxVideoDuration();
                    minVideoDuration = option.getGDTMinVideoDuration();
                    // setVideoOption是可选的，开发者可根据需要选择是否配置
                    videoOption = GDTAdapterUtils.getGMVideoOption(option);

                    //指定点击 APP 广告后是否展示二次确认，可选项包括 Default（wifi 不展示，非wifi 展示），NoConfirm（所有情况不展示）
                    if (option.getDownAPPConfirmPolicy() == GDTExtraOption.DownAPPConfirmPolicy.TYPE_DEFAULT) {
                        nativeUnifiedAD.setDownAPPConfirmPolicy(DownAPPConfirmPolicy.Default);
                    } else if (option.getDownAPPConfirmPolicy() == GDTExtraOption.DownAPPConfirmPolicy.TYPE_NO_CONFIRM) {
                        nativeUnifiedAD.setDownAPPConfirmPolicy(DownAPPConfirmPolicy.NOConfirm);
                    }
                }

                if (minVideoDuration > 0) {
                    nativeUnifiedAD.setMinVideoDuration(minVideoDuration);
                }
                if (maxVideoDuration > 0) {
                    nativeUnifiedAD.setMaxVideoDuration(maxVideoDuration);
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
        return getBiddingType() == GMAdConstant.AD_TYPE_CLIENT_BIDING;
    }

    @Override
    public void receiveBidResult(boolean win, double winnerPrice, int loseReason, Map<String, Object> extra) {
        super.receiveBidResult(win, winnerPrice, loseReason, extra);
    }
}