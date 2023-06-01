package com.ads.demo.custom.gdt;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.ads.demo.util.ThreadUtils;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.mediation.MediationConstant;
import com.bytedance.sdk.openadsdk.mediation.bridge.custom.reward.MediationCustomRewardVideoLoader;
import com.bytedance.sdk.openadsdk.mediation.custom.MediationCustomServiceConfig;
import com.bytedance.sdk.openadsdk.mediation.custom.MediationRewardItem;
import com.qq.e.ads.rewardvideo.RewardVideoAD;
import com.qq.e.ads.rewardvideo.RewardVideoADListener;
import com.qq.e.comm.util.AdError;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * YLH 激励视频广告自定义Adapter
 */
public class GdtCustomerReward extends MediationCustomRewardVideoLoader {

    private static final String TAG = "TTMediationSDK_" + GdtCustomerReward.class.getSimpleName();

    private volatile RewardVideoAD mRewardVideoAD;
    private boolean isLoadSuccess;

    @Override
    public void load(Context context, AdSlot adSlot, MediationCustomServiceConfig serviceConfig) {

        /**
         * 在子线程中进行广告加载
         */
        ThreadUtils.runOnThreadPool(new Runnable() {
            @Override
            public void run() {
                RewardVideoADListener rewardVideoADListener = new RewardVideoADListener() {
                    @Override
                    public void onADLoad() {
                        isLoadSuccess = true;
                        Log.i(TAG, "onADLoad");
                        if (isClientBidding()) {//bidding类型广告
                            double ecpm = mRewardVideoAD.getECPM(); //当无权限调用该接口时，SDK会返回错误码-1
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
                    public void onADShow() {
                        Log.i(TAG, "onADShow");
                        callRewardVideoAdShow();
                    }

                    @Override
                    public void onADExpose() {
                        Log.i(TAG, "onADExpose");
                    }

                    @Override
                    public void onReward(Map<String, Object> map) {
                        Log.i(TAG, "onReward");
                        callRewardVideoRewardVerify(new MediationRewardItem() {
                            @Override
                            public boolean rewardVerify() {
                                return true;
                            }

                            @Override
                            public float getAmount() {
                                return 0;
                            }

                            @Override
                            public String getRewardName() {
                                return null;
                            }

                            @Override
                            public Map<String, Object> getCustomData() {
                                return map;
                            }
                        });
                    }

                    @Override
                    public void onADClick() {
                        Log.i(TAG, "onADClick");
                        callRewardVideoAdClick();
                    }

                    @Override
                    public void onVideoComplete() {
                        Log.i(TAG, "onVideoComplete");
                        callRewardVideoComplete();
                    }

                    @Override
                    public void onADClose() {
                        Log.i(TAG, "onADClose");
                        callRewardVideoAdClosed();
                    }

                    @Override
                    public void onError(AdError adError) {
                        isLoadSuccess = false;
                        if (adError != null) {
                            Log.i(TAG, "onNoAD errorCode = " + adError.getErrorCode() + " errorMessage = " + adError.getErrorMsg());
                            callLoadFail(adError.getErrorCode(), adError.getErrorMsg());
                        } else {
                            callLoadFail(40000, "no ad");
                        }
                    }
                };

                boolean isMuted = adSlot.getMediationAdSlot() == null ? false : adSlot.getMediationAdSlot().isMuted();
                if (isServerBidding()) {
                    mRewardVideoAD = new RewardVideoAD(context, serviceConfig.getADNNetworkSlotId(), rewardVideoADListener, !isMuted, getAdm());
                } else {
                    mRewardVideoAD = new RewardVideoAD(context, serviceConfig.getADNNetworkSlotId(), rewardVideoADListener, !isMuted);
                }
                mRewardVideoAD.loadAD();
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
                if (mRewardVideoAD != null) {
                    if (isServerBidding()) {
                        mRewardVideoAD.setBidECPM(mRewardVideoAD.getECPM());
                    }
                    mRewardVideoAD.showAD(activity);
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
                if (mRewardVideoAD != null && mRewardVideoAD.isValid()) {
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
        mRewardVideoAD = null;
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
