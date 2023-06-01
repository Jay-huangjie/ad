package com.ads.demo.custom.gdt;


import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.ads.demo.util.ThreadUtils;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.mediation.MediationConstant;
import com.bytedance.sdk.openadsdk.mediation.ad.MediationNativeAdAppInfo;
import com.bytedance.sdk.openadsdk.mediation.ad.MediationViewBinder;
import com.bytedance.sdk.openadsdk.mediation.bridge.custom.draw.MediationCustomDrawAd;
import com.qq.e.ads.cfg.VideoOption;
import com.qq.e.ads.nativ.MediaView;
import com.qq.e.ads.nativ.NativeADEventListener;
import com.qq.e.ads.nativ.NativeADMediaListener;
import com.qq.e.ads.nativ.NativeUnifiedADAppMiitInfo;
import com.qq.e.ads.nativ.NativeUnifiedADData;
import com.qq.e.ads.nativ.widget.NativeAdContainer;
import com.qq.e.comm.constants.AdPatternType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhy on date
 * Usage:
 * Doc:
 */
public class GdtDrawAd extends MediationCustomDrawAd {
    private static final String TAG = GdtDrawAd.class.getSimpleName();
    public static final String TT_GDT_NATIVE_VIEW_TAG = "tt_gdt_developer_view";
    public static final String TT_GDT_NATIVE_ROOT_VIEW_TAG = "tt_gdt_developer_view_root";
    public static final String TT_GDT_NATIVE_LOGO_VIEW_TAG = "tt_gdt_developer_view_logo";

    private NativeUnifiedADData mNativeAdData;
    private VideoOption mVideoOption;

    public GdtDrawAd(NativeUnifiedADData data, VideoOption videoOption) {
        this.mNativeAdData = data;
        mVideoOption = videoOption;
        NativeUnifiedADAppMiitInfo info = mNativeAdData.getAppMiitInfo();
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
        this.setTitle(mNativeAdData.getTitle());
        this.setDescription(mNativeAdData.getDesc());
        this.setActionText(mNativeAdData.getCTAText());
        this.setIconUrl(mNativeAdData.getIconUrl());
        this.setImageUrl(mNativeAdData.getImgUrl());
        this.setImageWidth(mNativeAdData.getPictureWidth());
        this.setImageHeight(mNativeAdData.getPictureHeight());
        this.setImageList(mNativeAdData.getImgList());
        this.setStarRating(mNativeAdData.getAppScore());
        this.setSource(mNativeAdData.getTitle());
        this.setExpressAd(false);
        if (isClientBidding()) {
            int ecpm = mNativeAdData.getECPM();//当无权限调用该接口时，SDK会返回错误码-1
            if (ecpm < 0) {
                ecpm = 0;
            }
            setBiddingPrice(ecpm);//获取本条广告实时的eCPM价格，单位是分；
            Log.d(TAG, "GDT_clientBidding draw 返回的 cpm价格：" + mNativeAdData.getECPM());
        }

        if (mNativeAdData.getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
            this.setAdImageMode(TTAdConstant.IMAGE_MODE_VIDEO);
        } else if (mNativeAdData.getAdPatternType() == AdPatternType.NATIVE_1IMAGE_2TEXT
                || mNativeAdData.getAdPatternType() == AdPatternType.NATIVE_2IMAGE_2TEXT) {
            this.setAdImageMode(TTAdConstant.IMAGE_MODE_LARGE_IMG);
        } else if (mNativeAdData.getAdPatternType() == AdPatternType.NATIVE_3IMAGE) {
            this.setAdImageMode(TTAdConstant.IMAGE_MODE_GROUP_IMG);
        }

        if (mNativeAdData.isAppAd()) {
            setInteractionType(TTAdConstant.INTERACTION_TYPE_DOWNLOAD);
        } else {
            setInteractionType(TTAdConstant.INTERACTION_TYPE_LANDING_PAGE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause");
    }

    private void registerView(Context context, @NonNull ViewGroup container, List<View> clickViews, List<View> creativeViews, MediationViewBinder viewBinder) {
        if (mNativeAdData != null && container instanceof FrameLayout) {
            FrameLayout nativeAdView = (FrameLayout) container;


            NativeAdContainer gdtNativeAdContainer;

            /**
             *
             * 防止gdtNativeAdContainer重复添加的问题
             * if true 表示 gdtNativeAdContainer已经添加到了FrameLayout ，就无须重复常见 ，但需要移除gdt往gdtNativeAdContainer添加的view(广点通的logo)
             * if false 表示 gdtNativeAdContainer没有添加到FrameLayout ，需要创建 gdtNativeAdContainer
             *
             */
            if (nativeAdView.getChildAt(0) instanceof NativeAdContainer) {
                gdtNativeAdContainer = (NativeAdContainer) nativeAdView.getChildAt(0);
                for (int i = 0; i < gdtNativeAdContainer.getChildCount(); ) {
                    View view = gdtNativeAdContainer.getChildAt(i);
                    if (view != null) {
                        Object tag = view.getTag(com.bytedance.msdk.adapter.gdt.R.id.tt_mediation_gdt_developer_view_tag_key);
                        if (tag != null && (tag instanceof String) && ((String) tag).equals(TT_GDT_NATIVE_VIEW_TAG)) {
                            i++;
                        } else {
                            gdtNativeAdContainer.removeView(view);
                        }
                    } else {
                        i++;
                    }
                }
            } else {
                gdtNativeAdContainer = new NativeAdContainer(context);
                gdtNativeAdContainer.setTag(com.bytedance.msdk.adapter.gdt.R.id.tt_mediation_gdt_developer_view_root_tag_key, TT_GDT_NATIVE_ROOT_VIEW_TAG);
                for (int i = 0; i < nativeAdView.getChildCount(); ) {
                    View view = nativeAdView.getChildAt(i);
                    view.setTag(com.bytedance.msdk.adapter.gdt.R.id.tt_mediation_gdt_developer_view_tag_key, TT_GDT_NATIVE_VIEW_TAG);
                    final int index = nativeAdView.indexOfChild(view);
                    nativeAdView.removeViewInLayout(view);
                    if (view != null) {
                        final ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                        gdtNativeAdContainer.addView(view, index, layoutParams);
                    }

                }
                nativeAdView.removeAllViews();
                nativeAdView.addView(gdtNativeAdContainer, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            }


            //找出视频容器
            FrameLayout ttMediaView = nativeAdView.findViewById(viewBinder.mediaViewId);
            //绑定点击事件
            mNativeAdData.bindAdToView(context, gdtNativeAdContainer, null, clickViews, creativeViews);

            if (ttMediaView != null && mNativeAdData.getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
                MediaView gdtMediaView = new MediaView(context);
                ttMediaView.removeAllViews();
                ttMediaView.addView(gdtMediaView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                mNativeAdData.bindMediaView(gdtMediaView, mVideoOption, mGdtNativeADMediaListener);
            }

            if (!TextUtils.isEmpty(mNativeAdData.getCTAText())) {
                View view = nativeAdView.findViewById(viewBinder.callToActionId);
                List<View> CTAViews = new ArrayList<>();
                CTAViews.add(view);
                mNativeAdData.bindCTAViews(CTAViews);
            }


            mNativeAdData.setNativeAdEventListener(new NativeADEventListener() {
                @Override
                public void onADExposed() {
                    callAdShow();
                    Log.d(TAG, "draw GDT --- onADExposed。。。。");
                }

                @Override
                public void onADClicked() {
                    Log.d(TAG, "draw GDT --- onADClicked。。。。");
                    callAdClick();
                }

                @Override
                public void onADError(com.qq.e.comm.util.AdError error) {
                    Log.d(TAG, "GDT --- onADError error code :" + error.getErrorCode()
                            + "  error msg: " + error.getErrorMsg());
                }

                @Override
                public void onADStatusChanged() {
                    //app 下载状态改变
                }
            });

        }
    }


    @Override
    public void registerView(Activity activity,
                             ViewGroup container,
                             List<View> clickViews,
                             List<View> creativeViews,
                             List<View> directDownloadViews,
                             MediationViewBinder viewBinder) {
        super.registerView(activity, container, clickViews, creativeViews, directDownloadViews, viewBinder);
        ThreadUtils.runOnUIThreadByThreadPool(new Runnable() {
            @Override
            public void run() {
                if (creativeViews != null && directDownloadViews != null) {
                    creativeViews.addAll(directDownloadViews);
                }
                registerView(activity, container, clickViews, creativeViews, viewBinder);
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
    }

    @Override
    public MediationConstant.AdIsReadyStatus isReadyCondition() {
        /**
         * 在子线程中进行广告是否可用的判断
         */
        Future<MediationConstant.AdIsReadyStatus> future = ThreadUtils.runOnThreadPool(new Callable<MediationConstant.AdIsReadyStatus>() {
            @Override
            public MediationConstant.AdIsReadyStatus call() throws Exception {
                if (mNativeAdData != null && mNativeAdData.isValid()) {
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

    NativeADMediaListener mGdtNativeADMediaListener = new NativeADMediaListener() {
        @Override
        public void onVideoInit() {
            Log.d(TAG, "onVideoInit: ");
        }

        @Override
        public void onVideoLoading() {
            Log.d(TAG, "onVideoLoading: ");
        }

        @Override
        public void onVideoReady() {
            Log.d(TAG, "onVideoReady");
        }

        @Override
        public void onVideoLoaded(int videoDuration) {
            Log.d(TAG, "onVideoLoaded: ");
        }

        @Override
        public void onVideoStart() {
            Log.d(TAG, "onVideoStart");
            callVideoStart();
        }

        @Override
        public void onVideoPause() {
            callVideoPause();
        }

        @Override
        public void onVideoResume() {
            callVideoResume();
        }

        @Override
        public void onVideoCompleted() {
            callVideoCompleted();
        }

        @Override
        public void onVideoError(com.qq.e.comm.util.AdError error) {
            if (error != null) {
                callVideoError(error.getErrorCode(), error.getErrorMsg());
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
    };
}

