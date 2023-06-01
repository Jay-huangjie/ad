package com.ads.demo.custom.gdt;

import android.content.Context;
import android.util.Log;

import com.ads.demo.util.ThreadUtils;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.mediation.MediationConstant;
import com.bytedance.sdk.openadsdk.mediation.bridge.custom.native_ad.MediationCustomNativeLoader;
import com.bytedance.sdk.openadsdk.mediation.custom.MediationCustomServiceConfig;
import com.qq.e.ads.nativ.ADSize;
import com.qq.e.ads.nativ.NativeADUnifiedListener;
import com.qq.e.ads.nativ.NativeExpressAD;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.ads.nativ.NativeUnifiedAD;
import com.qq.e.ads.nativ.NativeUnifiedADData;
import com.qq.e.comm.util.AdError;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * YLH 信息流广告自定义Adapter
 */
public class GdtCustomerNative extends MediationCustomNativeLoader {

    private static final String TAG = "TTMediationSDK_" + GdtCustomerNative.class.getSimpleName();

    @Override
    public void load(Context context, AdSlot adSlot, MediationCustomServiceConfig serviceConfig) {
        /**
         * 在子线程中进行广告加载
         */
        Log.e(TAG, "load gdt custom native ad-----");
        ThreadUtils.runOnThreadPool(new Runnable() {
            @Override
            public void run() {
                if (isNativeAd()) {
                    Log.i(TAG, "自渲染");
                    //自渲染类型
                    NativeUnifiedAD nativeUnifiedAD = null;
                    NativeADUnifiedListener nativeADUnifiedListener = new NativeADUnifiedListener() {
                        @Override
                        public void onADLoaded(List<NativeUnifiedADData> list) {
                            List<GdtNativeAd> tempList = new ArrayList<>();
                            for (NativeUnifiedADData feedAd : list) {
                                GdtNativeAd gdtNativeAd = new GdtNativeAd(context, feedAd, adSlot);
                                //添加扩展参数
                                Map<String, Object> extraMsg = new HashMap<>();
                                extraMsg.put("key1_自渲染", "value1_自渲染");
                                extraMsg.put("key2_自渲染", "value2_自渲染");
                                extraMsg.put("key3_自渲染", "value3_自渲染");
                                gdtNativeAd.setMediaExtraInfo(extraMsg);

                                if (isClientBidding()) {//bidding广告类型
                                    double ecpm = feedAd.getECPM();//当无权限调用该接口时，SDK会返回错误码-1
                                    if (ecpm < 0) {
                                        ecpm = 0;
                                    }
                                    Log.e(TAG, "ecpm:" + ecpm);
                                    gdtNativeAd.setBiddingPrice(ecpm); //回传竞价广告价格
                                }
                                tempList.add(gdtNativeAd);
                            }
                            callLoadSuccess(tempList);
                        }

                        @Override
                        public void onNoAD(AdError adError) {
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

                    int maxVideoDuration = GdtUtils.getGDTMaxVideoDuration(adSlot);
                    int minVideoDuration = GdtUtils.getGDTMinVideoDuration(adSlot);
                    if (maxVideoDuration > 0) {
                        nativeUnifiedAD.setMaxVideoDuration(maxVideoDuration);
                    }
                    if (minVideoDuration > 0) {
                        nativeUnifiedAD.setMinVideoDuration(minVideoDuration);
                    }
                    nativeUnifiedAD.loadData(1);
                } else if (isExpressRender()) {
                    Log.i(TAG, "模板");
                    //模板类型
                    NativeExpressAD nativeExpressAD = null;
                    NativeExpressAD.NativeExpressADListener nativeExpressADListener = new NativeExpressAD.NativeExpressADListener() {
                        private Map<NativeExpressADView, GdtNativeExpressAd> mListenerMap = new HashMap<>();

                        @Override
                        public void onADLoaded(List<NativeExpressADView> list) {
                            List<GdtNativeExpressAd> tempList = new ArrayList<>();
                            for (NativeExpressADView feedAd : list) {
                                GdtNativeExpressAd gdtNativeAd = new GdtNativeExpressAd(feedAd, adSlot);
                                //添加扩展参数
                                Map<String, Object> extraMsg = new HashMap<>();
                                extraMsg.put("key1_模板", "value1_模板");
                                extraMsg.put("key2_模板", "value2_模板");
                                extraMsg.put("key3_模板", "value3_模板");
                                gdtNativeAd.setMediaExtraInfo(extraMsg);

                                if (isClientBidding()) {//bidding广告类型
                                    double ecpm = feedAd.getECPM();//当无权限调用该接口时，SDK会返回错误码-1
                                    if (ecpm < 0) {
                                        ecpm = 0;
                                    }
                                    Log.e(TAG, "ecpm:" + ecpm);
                                    gdtNativeAd.setBiddingPrice(ecpm); //回传竞价广告价格
                                }
                                mListenerMap.put(feedAd, gdtNativeAd);
                                tempList.add(gdtNativeAd);
                            }
                            callLoadSuccess(tempList);
                        }

                        @Override
                        public void onRenderFail(NativeExpressADView nativeExpressADView) {
                            Log.i(TAG, "onRenderFail");
                            GdtNativeExpressAd gdtNativeAd = mListenerMap.get(nativeExpressADView);
                            if (gdtNativeAd != null) {
                                gdtNativeAd.callRenderFail(nativeExpressADView, 99999, "render fail");
                            }
                        }

                        @Override
                        public void onRenderSuccess(NativeExpressADView nativeExpressADView) {
                            Log.i(TAG, "onRenderSuccess");
                            GdtNativeExpressAd gdtNativeAd = mListenerMap.get(nativeExpressADView);
                            if (gdtNativeAd != null) {
                                gdtNativeAd.callRenderSuccess(ADSize.FULL_WIDTH, ADSize.AUTO_HEIGHT);
                            }
                        }

                        @Override
                        public void onADExposure(NativeExpressADView nativeExpressADView) {
                            Log.i(TAG, "onADExposure");
                            GdtNativeExpressAd gdtNativeAd = mListenerMap.get(nativeExpressADView);
                            if (gdtNativeAd != null) {
                                gdtNativeAd.callAdShow();
                            }
                        }

                        @Override
                        public void onADClicked(NativeExpressADView nativeExpressADView) {
                            Log.i(TAG, "onADClicked");
                            GdtNativeExpressAd gdtNativeAd = mListenerMap.get(nativeExpressADView);
                            if (gdtNativeAd != null) {
                                gdtNativeAd.callAdClick();
                            }
                        }

                        @Override
                        public void onADClosed(NativeExpressADView nativeExpressADView) {
                            Log.i(TAG, "onADClosed");
                            GdtNativeExpressAd gdtNativeAd = mListenerMap.get(nativeExpressADView);
                            if (gdtNativeAd != null) {
                                gdtNativeAd.onDestroy();
                            }
                            mListenerMap.remove(nativeExpressADView);
                        }

                        @Override
                        public void onADLeftApplication(NativeExpressADView nativeExpressADView) {
                            Log.i(TAG, "onADLeftApplication");
                        }

                        @Override
                        public void onNoAD(AdError adError) {
                            if (adError != null) {
                                Log.i(TAG, "onNoAD errorCode = " + adError.getErrorCode() + " errorMessage = " + adError.getErrorMsg());
                                callLoadFail(adError.getErrorCode(), adError.getErrorMsg());
                            } else {
                                callLoadFail(40000, "no ad");
                            }
                        }
                    };

                    if (isServerBidding()) {
                        nativeExpressAD = new NativeExpressAD(context, getAdSize(adSlot), serviceConfig.getADNNetworkSlotId(), nativeExpressADListener, getAdm());
                    } else {
                        nativeExpressAD = new NativeExpressAD(context, getAdSize(adSlot), serviceConfig.getADNNetworkSlotId(), nativeExpressADListener);
                    }
                    nativeExpressAD.loadAD(1);
                } else {
                    Log.i(TAG, "其他类型");
                    //其他类型，开发者如果有需要，请在平台自行配置json,然后通过 serviceConfig.getCustomAdapterJson() 获取配置

                }
            }
        });
    }

    private ADSize getAdSize(AdSlot adSlot) {
        ADSize adSize = new ADSize(ADSize.FULL_WIDTH, ADSize.AUTO_HEIGHT); // 消息流中用AUTO_HEIGHT
        if (adSlot.getImgAcceptedWidth() > 0) {
            adSize = new ADSize(adSlot.getImgAcceptedWidth(), ADSize.AUTO_HEIGHT);
        }
        return adSize;
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
