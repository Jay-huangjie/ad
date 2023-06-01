package com.ads.demo.custom.gdt;

import android.util.Log;
import android.view.View;

import com.ads.demo.util.ThreadUtils;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.mediation.MediationConstant;
import com.bytedance.sdk.openadsdk.mediation.bridge.custom.native_ad.MediationCustomNativeAd;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.ads.nativ.NativeExpressMediaListener;
import com.qq.e.comm.constants.AdPatternType;
import com.qq.e.comm.pi.AdData;
import com.qq.e.comm.util.AdError;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * YLH 信息流 ADN提供渲染（模板渲染）广告对象
 */
public class GdtNativeExpressAd extends MediationCustomNativeAd {

    private static final String TAG = GdtNativeExpressAd.class.getSimpleName();

    private NativeExpressADView mNativeExpressADView;

    public GdtNativeExpressAd(NativeExpressADView feedAd, AdSlot adSlot) {
        setExpressAd(true);
        mNativeExpressADView = feedAd;
        AdData adData = mNativeExpressADView.getBoundData();
        if (adData.getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
            mNativeExpressADView.preloadVideo();
            mNativeExpressADView.setMediaListener(new NativeExpressMediaListener() {
                @Override
                public void onVideoInit(NativeExpressADView nativeExpressADView) {
                    Log.i(TAG, "onVideoInit");
                }

                @Override
                public void onVideoLoading(NativeExpressADView nativeExpressADView) {
                    Log.i(TAG, "onVideoLoading");
                }

                @Override
                public void onVideoCached(NativeExpressADView nativeExpressADView) {
                    Log.i(TAG, "onVideoCached");
                }

                @Override
                public void onVideoReady(NativeExpressADView nativeExpressADView, long l) {
                    Log.i(TAG, "onVideoReady");
                }

                @Override
                public void onVideoStart(NativeExpressADView nativeExpressADView) {
                    Log.i(TAG, "onVideoStart");
                    callVideoStart();
                }

                @Override
                public void onVideoPause(NativeExpressADView nativeExpressADView) {
                    Log.i(TAG, "onVideoPause");
                    callVideoPause();
                }

                @Override
                public void onVideoComplete(NativeExpressADView nativeExpressADView) {
                    Log.i(TAG, "onVideoComplete");
                    callVideoCompleted();
                }

                @Override
                public void onVideoError(NativeExpressADView nativeExpressADView, AdError adError) {
                    Log.i(TAG, "onVideoError");
                    if (adError != null) {
                        callVideoError(adError.getErrorCode(), adError.getErrorMsg());
                    } else {
                        callVideoError(99999, "video error");
                    }
                }

                @Override
                public void onVideoPageOpen(NativeExpressADView nativeExpressADView) {
                    Log.i(TAG, "onVideoPageOpen");
                }

                @Override
                public void onVideoPageClose(NativeExpressADView nativeExpressADView) {
                    Log.i(TAG, "onVideoPageClose");
                }
            });
            setAdImageMode(TTAdConstant.IMAGE_MODE_VIDEO);
        } else if (adData.getAdPatternType() == AdPatternType.NATIVE_1IMAGE_2TEXT || adData.getAdPatternType() == AdPatternType.NATIVE_2IMAGE_2TEXT) {
            setAdImageMode(TTAdConstant.IMAGE_MODE_LARGE_IMG);
        } else if (adData.getAdPatternType() == AdPatternType.NATIVE_3IMAGE) {
            setAdImageMode(TTAdConstant.IMAGE_MODE_GROUP_IMG);
        } else {
            setAdImageMode(TTAdConstant.IMAGE_MODE_LARGE_IMG);
        }
        setTitle(adData.getTitle());
        setDescription(adData.getDesc());
        setInteractionType(TTAdConstant.INTERACTION_TYPE_LANDING_PAGE);
    }

    /**
     * 如果Adn 有dislike接口需要返回true
     */
    @Override
    public boolean hasDislike() {
        return true;
    }

    @Override
    public void render() {
        /**
         * 先切子线程，再在子线程中切主线程进行广告展示
         */
        ThreadUtils.runOnUIThreadByThreadPool(new Runnable() {
            @Override
            public void run() {
                if (mNativeExpressADView != null) {
                    mNativeExpressADView.render();
                }
            }
        });
    }

    @Override
    public View getExpressView() {
        if (isServerBidding()) { //曝光扣费, 单位分，若优量汇竞胜，在广告曝光时回传，必传
            mNativeExpressADView.setBidECPM(mNativeExpressADView.getECPM());
        }
        return mNativeExpressADView;
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
        /**
         * 在子线程进行onDestroy操作
         */
        ThreadUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "onDestroy");
                if (mNativeExpressADView != null) {
                    mNativeExpressADView.destroy();
                    mNativeExpressADView = null;
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
                if (mNativeExpressADView != null && mNativeExpressADView.isValid()) {
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
}
