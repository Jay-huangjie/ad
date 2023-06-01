package com.ads.demo.custom.gdt;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ads.demo.util.ThreadUtils;
import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.mediation.MediationConstant;
import com.bytedance.sdk.openadsdk.mediation.ad.MediationNativeAdAppInfo;
import com.bytedance.sdk.openadsdk.mediation.ad.MediationViewBinder;
import com.bytedance.sdk.openadsdk.mediation.bridge.custom.native_ad.MediationCustomNativeAd;
import com.qq.e.ads.nativ.MediaView;
import com.qq.e.ads.nativ.NativeADEventListener;
import com.qq.e.ads.nativ.NativeADMediaListener;
import com.qq.e.ads.nativ.NativeUnifiedADAppMiitInfo;
import com.qq.e.ads.nativ.NativeUnifiedADData;
import com.qq.e.ads.nativ.widget.NativeAdContainer;
import com.qq.e.comm.constants.AdPatternType;
import com.qq.e.comm.util.AdError;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * YLH 信息流 开发者自渲染（自渲染）广告对象
 */
public class GdtNativeAd extends MediationCustomNativeAd {

    private static final String TAG = GdtNativeAd.class.getSimpleName();

    private NativeUnifiedADData mNativeUnifiedADData;
    private AdSlot mAdSlot;
    private Context mContext;
    private String VIEW_TAG = "view_tag";
    private boolean statusFlag = true; //app下载状态记录标识

    public GdtNativeAd(Context context, NativeUnifiedADData feedAd, AdSlot adSlot) {
        mContext = context;
        mNativeUnifiedADData = feedAd;
        mAdSlot = adSlot;
        NativeUnifiedADAppMiitInfo info = mNativeUnifiedADData.getAppMiitInfo();
        MediationNativeAdAppInfo nativeAdAppInfo = new MediationNativeAdAppInfo();
        if (info != null) {
            nativeAdAppInfo.setAppName(info.getAppName());
            nativeAdAppInfo.setAuthorName(info.getAuthorName());
            nativeAdAppInfo.setPackageSizeBytes(info.getPackageSizeBytes());
            nativeAdAppInfo.setPermissionsUrl(info.getPermissionsUrl());
            nativeAdAppInfo.setPrivacyAgreement(info.getPrivacyAgreement());
            nativeAdAppInfo.setVersionName(info.getVersionName());
        }
        setNativeAdAppInfo(nativeAdAppInfo);
        setTitle(mNativeUnifiedADData.getTitle());
        setDescription(mNativeUnifiedADData.getDesc());
        setActionText(mNativeUnifiedADData.getCTAText());
        setIconUrl(mNativeUnifiedADData.getIconUrl());
        setImageUrl(mNativeUnifiedADData.getImgUrl());
        setImageWidth(mNativeUnifiedADData.getPictureWidth());
        setImageHeight(mNativeUnifiedADData.getPictureHeight());
        setImageList(mNativeUnifiedADData.getImgList());
        setStarRating(mNativeUnifiedADData.getAppScore());
        setSource(mNativeUnifiedADData.getTitle());

        if (mNativeUnifiedADData.getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
            setAdImageMode(TTAdConstant.IMAGE_MODE_VIDEO);
        } else if (mNativeUnifiedADData.getAdPatternType() == AdPatternType.NATIVE_1IMAGE_2TEXT
                || mNativeUnifiedADData.getAdPatternType() == AdPatternType.NATIVE_2IMAGE_2TEXT) {
            setAdImageMode(TTAdConstant.IMAGE_MODE_LARGE_IMG);
        } else if (mNativeUnifiedADData.getAdPatternType() == AdPatternType.NATIVE_3IMAGE) {
            setAdImageMode(TTAdConstant.IMAGE_MODE_GROUP_IMG);
        }

        if (mNativeUnifiedADData.isAppAd()) {
            setInteractionType(TTAdConstant.INTERACTION_TYPE_DOWNLOAD);
        } else {
            setInteractionType(TTAdConstant.INTERACTION_TYPE_LANDING_PAGE);
        }
    }


    @Override
    public void registerView(Activity activity,
                             ViewGroup container,
                             List<View> clickViews,
                             List<View> creativeViews,
                             List<View> directDownloadViews,
                             MediationViewBinder viewBinder) {
        /**
         * 先切子线程，再在子线程中切主线程进行广告展示
         */
        ThreadUtils.runOnUIThreadByThreadPool(new Runnable() {
            @Override
            public void run() {
                if (isServerBidding()) { //曝光扣费, 单位分，若优量汇竞胜，在广告曝光时回传，必传
                    mNativeUnifiedADData.setBidECPM(mNativeUnifiedADData.getECPM());
                }

                if (mNativeUnifiedADData != null && container instanceof FrameLayout) {
                    FrameLayout nativeAdView = (FrameLayout) container;
                    NativeAdContainer nativeAdContainer;

                    if (nativeAdView.getChildAt(0) instanceof NativeAdContainer) {
                        //gdt会自动添加logo，会出现重复添加，需要把logo移除
                        nativeAdContainer = (NativeAdContainer) nativeAdView.getChildAt(0);
                        for (int i = 0; i < nativeAdContainer.getChildCount(); ) {
                            View view = nativeAdContainer.getChildAt(i);
                            if (view != null) {
                                Object tag = view.getTag();
                                if (tag != null && (tag instanceof String) && ((String) tag).equals(VIEW_TAG)) {
                                    i++;
                                } else {
                                    nativeAdContainer.removeView(view);
                                }
                            } else {
                                i++;
                            }
                        }
                    } else {
                        nativeAdContainer = new NativeAdContainer(mContext);
                        for (int i = 0; i < nativeAdView.getChildCount(); ) {
                            View view = nativeAdView.getChildAt(i);
                            view.setTag(VIEW_TAG);
                            final int index = nativeAdView.indexOfChild(view);
                            nativeAdView.removeViewInLayout(view);
                            final ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                            nativeAdContainer.addView(view, index, layoutParams);
                        }
                        nativeAdView.removeAllViews();
                        nativeAdView.addView(nativeAdContainer, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    }

                    if (creativeViews != null && directDownloadViews != null) { //若传入的参数中有directView，需要做处理
                        creativeViews.addAll(directDownloadViews);
                    }


                    mNativeUnifiedADData.bindAdToView(activity, nativeAdContainer, GdtUtils.getNativeAdLogoParams(mAdSlot), clickViews, creativeViews);


                    FrameLayout ttMediaView = nativeAdView.findViewById(viewBinder.mediaViewId);

                    if (ttMediaView != null && mNativeUnifiedADData.getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
                        MediaView gdtMediaView = new MediaView(mContext);
                        ttMediaView.removeAllViews();
                        ttMediaView.addView(gdtMediaView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        mNativeUnifiedADData.bindMediaView(gdtMediaView,GdtUtils.getGMVideoOption(mAdSlot), new NativeADMediaListener() {
                            @Override
                            public void onVideoInit() {
                                Log.d(TAG, "onVideoInit");
                            }

                            @Override
                            public void onVideoLoading() {
                                Log.d(TAG, "onVideoLoading");
                            }

                            @Override
                            public void onVideoReady() {
                                Log.d(TAG, "onVideoReady");
                            }

                            @Override
                            public void onVideoLoaded(int i) {
                                Log.d(TAG, "onVideoLoaded");
                            }

                            @Override
                            public void onVideoStart() {
                                Log.d(TAG, "onVideoStart");
                                callVideoStart();
                            }

                            @Override
                            public void onVideoPause() {
                                Log.d(TAG, "onVideoPause");
                                callVideoPause();
                            }

                            @Override
                            public void onVideoResume() {
                                Log.d(TAG, "onVideoResume");
                                callVideoResume();
                            }

                            @Override
                            public void onVideoCompleted() {
                                Log.d(TAG, "onVideoCompleted");
                                callVideoCompleted();
                            }

                            @Override
                            public void onVideoError(AdError adError) {
                                if (adError != null) {
                                    Log.i(TAG, "onVideoError errorCode = " + adError.getErrorCode() + " errorMessage = " + adError.getErrorMsg());
                                    callVideoError(adError.getErrorCode(), adError.getErrorMsg());
                                } else {
                                    callVideoError(99999, "video error");
                                }
                            }

                            @Override
                            public void onVideoStop() {
                                Log.d(TAG, "onVideoStop");
                            }

                            @Override
                            public void onVideoClicked() {
                                Log.d(TAG, "onVideoClicked");
                                callAdClick();
                            }
                        });
                    }
                    if (!TextUtils.isEmpty(mNativeUnifiedADData.getCTAText())) {
                        View view = nativeAdView.findViewById(viewBinder.callToActionId);
                        List<View> CTAViews = new ArrayList<>();
                        CTAViews.add(view);
                        mNativeUnifiedADData.bindCTAViews(CTAViews);
                    }
                    mNativeUnifiedADData.setNativeAdEventListener(new NativeADEventListener() {
                        @Override
                        public void onADExposed() {
                            Log.d(TAG, "onADExposed");
                            callAdShow();
                        }

                        @Override
                        public void onADClicked() {
                            Log.d(TAG, "onADClicked");
                            callAdClick();
                        }

                        @Override
                        public void onADError(AdError adError) {
                            Log.d(TAG, "onADError");
                        }

                        @Override
                        public void onADStatusChanged() {

                        }
                    });
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        /**
         * 先切子线程，再在子线程中切主线程进行onPause操作
         */
        ThreadUtils.runOnUIThreadByThreadPool(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "onPause");
                if (mNativeUnifiedADData != null) {
                    mNativeUnifiedADData.pauseVideo();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        /**
         * 先切子线程，再在子线程中切主线程进行onResume操作
         */
        ThreadUtils.runOnUIThreadByThreadPool(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "onResume");
                if (mNativeUnifiedADData != null) {
                    mNativeUnifiedADData.resume();
                }
            }
        });
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
                if (mNativeUnifiedADData != null) {
                    mNativeUnifiedADData.destroy();
                    mNativeUnifiedADData = null;
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
                if (mNativeUnifiedADData != null && mNativeUnifiedADData.isValid()) {
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
