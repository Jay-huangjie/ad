package com.ads.demo.custom.gdt;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.ads.demo.util.ThreadUtils;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.mediation.MediationConstant;
import com.bytedance.sdk.openadsdk.mediation.bridge.custom.fullvideo.MediationCustomFullVideoLoader;
import com.bytedance.sdk.openadsdk.mediation.custom.MediationCustomServiceConfig;
import com.bytedance.sdk.openadsdk.mediation.custom.MediationRewardItem;
import com.qq.e.ads.interstitial2.ADRewardListener;
import com.qq.e.ads.interstitial2.UnifiedInterstitialAD;
import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener;
import com.qq.e.ads.interstitial2.UnifiedInterstitialMediaListener;
import com.qq.e.comm.util.AdError;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * YLH 全屏广告自定义Adapter
 */
public class GdtCustomerFullVideo extends MediationCustomFullVideoLoader {

    private static final String TAG = GdtCustomerFullVideo.class.getSimpleName();

    private volatile UnifiedInterstitialAD mUnifiedInterstitialAD;
    private boolean isLoadSuccess;

    @Override
    public void load(Context context, AdSlot adSlot, MediationCustomServiceConfig serviceConfig) {
        /**
         * 在子线程中进行广告加载
         */
        ThreadUtils.runOnThreadPool(new Runnable() {
            @Override
            public void run() {
                if (context instanceof Activity) {
                    mUnifiedInterstitialAD = new UnifiedInterstitialAD((Activity) context, serviceConfig.getADNNetworkSlotId(), new UnifiedInterstitialADListener() {
                        @Override
                        public void onADReceive() {
                            isLoadSuccess = true;
                            Log.i(TAG, "onADReceive");
                            if (isClientBidding()) {//bidding类型广告
                                double ecpm = mUnifiedInterstitialAD.getECPM(); //当无权限调用该接口时，SDK会返回错误码-1
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
                            callAdVideoCache();
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
                        public void onADOpened() {
                            Log.i(TAG, "onADOpened");
                        }

                        @Override
                        public void onADExposure() {
                            Log.i(TAG, "onADExposure");
                            callFullVideoAdShow();
                        }

                        @Override
                        public void onADClicked() {
                            Log.i(TAG, "onADClicked");
                            callFullVideoAdClick();
                        }

                        @Override
                        public void onADLeftApplication() {
                            Log.i(TAG, "onADLeftApplication");
                        }

                        @Override
                        public void onADClosed() {
                            Log.i(TAG, "onADClosed");
                            callFullVideoAdClosed();
                        }

                        @Override
                        public void onRenderSuccess() {
                            Log.i(TAG, "onRenderSuccess");
                        }

                        @Override
                        public void onRenderFail() {
                            Log.i(TAG, "onRenderFail");
                        }
                    });
                    mUnifiedInterstitialAD.setMediaListener(new UnifiedInterstitialMediaListener() {
                        @Override
                        public void onVideoInit() {
                            Log.i(TAG, "onVideoInit");
                        }

                        @Override
                        public void onVideoLoading() {
                            Log.i(TAG, "onVideoLoading");
                        }

                        @Override
                        public void onVideoReady(long l) {
                            Log.i(TAG, "onVideoReady");
                        }

                        @Override
                        public void onVideoStart() {
                            Log.i(TAG, "onVideoStart");
                        }

                        @Override
                        public void onVideoPause() {
                            Log.i(TAG, "onVideoPause");
                        }

                        @Override
                        public void onVideoComplete() {
                            Log.i(TAG, "onVideoComplete");
                            callFullVideoComplete();
                        }

                        @Override
                        public void onVideoError(AdError adError) {
                            Log.i(TAG, "onVideoError");
                            callFullVideoError();
                        }

                        @Override
                        public void onVideoPageOpen() {
                            Log.i(TAG, "onVideoPageOpen");
                        }

                        @Override
                        public void onVideoPageClose() {
                            Log.i(TAG, "onVideoPageClose");
                        }
                    });
                    mUnifiedInterstitialAD.setRewardListener(new ADRewardListener() {
                        @Override
                        public void onReward(Map<String, Object> map) {
                            Log.e(TAG, "onReward");
                            float amount = 0f;
                            String name = "";
                            float finalAmount = amount;
                            String finalName = name;
                            callFullVideoRewardVerify(new MediationRewardItem() {
                                @Override
                                public boolean rewardVerify() {
                                    return true;
                                }

                                @Override
                                public float getAmount() {
                                    return finalAmount;
                                }

                                @Override
                                public String getRewardName() {
                                    return finalName;
                                }

                                @Override
                                public Map<String, Object> getCustomData() {
                                    return map;
                                }
                            });
                        }
                    });
                    mUnifiedInterstitialAD.loadFullScreenAD();
                } else {
                    callLoadFail(40000, "context is not Activity");
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
                    mUnifiedInterstitialAD.showFullScreenAD(activity);
                }
            }
        });

    }

    @Override
    public MediationConstant.AdIsReadyStatus isReadyCondition() {
        /**
         * 在子线程中进行广告是否可用的判断
         */
        Future<MediationConstant.AdIsReadyStatus> future = ThreadUtils.runOnThreadPool(new Callable<MediationConstant.AdIsReadyStatus>() {
            @Override
            public MediationConstant.AdIsReadyStatus call() throws Exception {
                if (mUnifiedInterstitialAD != null && mUnifiedInterstitialAD.isValid()) {
                    return MediationConstant.AdIsReadyStatus.AD_IS_READY;
                } else {
                    return MediationConstant.AdIsReadyStatus.AD_IS_NOT_READY;
                }
            }
        });
        try {
            MediationConstant.AdIsReadyStatus result = future.get(500, TimeUnit.MILLISECONDS); //设置500毫秒的总超时，避免线程阻塞
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
     * 是否是ClientBidding广告
     *
     * @return
     */
    public boolean isClientBidding() {
        return getBiddingType() == MediationConstant.AD_TYPE_CLIENT_BIDING;
    }
}
