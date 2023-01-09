package com.ads.demo.custom.gdt;

import android.util.Log;
import android.view.View;

import com.ads.demo.AppConst;
import com.ads.demo.custom.Const;
import com.ads.demo.util.ThreadUtils;
import com.bytedance.msdk.api.v2.GMAdConstant;
import com.bytedance.msdk.api.v2.ad.custom.GMCustomAdError;
import com.bytedance.msdk.api.v2.ad.custom.nativeAd.GMCustomNativeAd;
import com.bytedance.msdk.api.v2.slot.GMAdSlotNative;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.qq.e.ads.nativ.NativeExpressMediaListener;
import com.qq.e.comm.constants.AdPatternType;
import com.qq.e.comm.pi.AdData;
import com.qq.e.comm.util.AdError;

/**
 * YLH 信息流 ADN提供渲染（模板渲染）广告对象
 */
public class GdtNativeExpressAd extends GMCustomNativeAd {

    private static final String TAG = AppConst.TAG_PRE + GdtNativeExpressAd.class.getSimpleName();

    private NativeExpressADView mNativeExpressADView;

    public GdtNativeExpressAd(NativeExpressADView feedAd, GMAdSlotNative adSlot) {
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
                    callNativeVideoStart();
                }

                @Override
                public void onVideoPause(NativeExpressADView nativeExpressADView) {
                    Log.i(TAG, "onVideoPause");
                    callNativeVideoPause();
                }

                @Override
                public void onVideoComplete(NativeExpressADView nativeExpressADView) {
                    Log.i(TAG, "onVideoComplete");
                    callNativeVideoCompleted();
                }

                @Override
                public void onVideoError(NativeExpressADView nativeExpressADView, AdError adError) {
                    Log.i(TAG, "onVideoError");
                    if (adError != null) {
                        callNativeVideoError(new GMCustomAdError(adError.getErrorCode(), adError.getErrorMsg()));
                    } else {
                        callNativeVideoError(new GMCustomAdError(Const.VIDEO_ERROR, "video error"));
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
            setAdImageMode(GMAdConstant.IMAGE_MODE_VIDEO);
        } else if (adData.getAdPatternType() == AdPatternType.NATIVE_1IMAGE_2TEXT || adData.getAdPatternType() == AdPatternType.NATIVE_2IMAGE_2TEXT) {
            setAdImageMode(GMAdConstant.IMAGE_MODE_LARGE_IMG);
        } else if (adData.getAdPatternType() == AdPatternType.NATIVE_3IMAGE) {
            setAdImageMode(GMAdConstant.IMAGE_MODE_GROUP_IMG);
        } else {
            setAdImageMode(GMAdConstant.IMAGE_MODE_LARGE_IMG);
        }
        setTitle(adData.getTitle());
        setDescription(adData.getDesc());
        setInteractionType(GMAdConstant.INTERACTION_TYPE_LANDING_PAGE);
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
    public GMAdConstant.AdIsReadyStatus isReadyStatus() {
        if (mNativeExpressADView != null && mNativeExpressADView.isValid()) {
            return GMAdConstant.AdIsReadyStatus.AD_IS_READY;
        } else {
            return GMAdConstant.AdIsReadyStatus.AD_IS_NOT_READY;
        }
    }
}