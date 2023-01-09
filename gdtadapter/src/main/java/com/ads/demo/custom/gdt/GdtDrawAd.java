package com.ads.demo.custom.gdt;

import static com.bytedance.msdk.adapter.TTAdConstant.TT_GDT_NATIVE_ROOT_VIEW_TAG;
import static com.bytedance.msdk.adapter.TTAdConstant.TT_GDT_NATIVE_VIEW_TAG;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.ads.demo.AppConst;
import com.bytedance.msdk.adapter.gdt.GDTAdapterUtils;
import com.bytedance.msdk.adapter.util.Logger;
import com.bytedance.msdk.api.TTAdConstant;
import com.bytedance.msdk.api.format.TTMediaView;
import com.bytedance.msdk.api.format.TTNativeAdView;
import com.bytedance.msdk.api.v2.GMAdConstant;
import com.bytedance.msdk.api.v2.ad.custom.GMCustomAdError;
import com.bytedance.msdk.api.v2.ad.custom.draw.GMCustomDrawAd;
import com.bytedance.msdk.api.v2.ad.nativeAd.GMNativeAdAppInfo;
import com.bytedance.msdk.api.v2.ad.nativeAd.GMViewBinder;
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

/**
 * Created by zhy on date
 * Usage:
 * Doc:
 */
public class GdtDrawAd extends GMCustomDrawAd {
    private static final String TAG = AppConst.TAG_PRE + GdtDrawAd.class.getSimpleName();

    private NativeUnifiedADData mNativeAdData;
    private VideoOption mVideoOption;

    public GdtDrawAd(NativeUnifiedADData data, GdtCustomerDraw drawAdapter, VideoOption videoOption) {
        this.mNativeAdData = data;
        mVideoOption = videoOption;
        NativeUnifiedADAppMiitInfo info = mNativeAdData.getAppMiitInfo();
        GMNativeAdAppInfo nativeAdAppInfo = new GMNativeAdAppInfo();
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
        if (mNativeAdData.isAppAd()) {
            setInteractionType(GMAdConstant.INTERACTION_TYPE_DOWNLOAD);
        } else {
            setInteractionType(GMAdConstant.INTERACTION_TYPE_LANDING_PAGE);
        }
        this.setExpressAd(false);
        if (drawAdapter != null) {
            if (drawAdapter.isClientBidding()) {
                this.setCpm(mNativeAdData.getECPM() != -1 ? (double) mNativeAdData.getECPM() : GDTAdapterUtils.CPM_DEFLAUT_VALUE);//获取本条广告实时的eCPM价格，单位是分；
                Logger.d(TAG, "GDT_clientBidding draw 返回的 cpm价格：" + mNativeAdData.getECPM());
            }
        }

        if (mNativeAdData.getAdPatternType() == AdPatternType.NATIVE_VIDEO) {
            this.setAdImageMode(TTAdConstant.IMAGE_MODE_VIDEO);
//            mNativeAdData.preloadVideo(new VideoPreloadListener() {
//                @Override
//                public void onVideoCacheFailed(int i, String s) {
//
//                }
//
//                @Override
//                public void onVideoCached() {
//
//                }
//            });
        } else if (mNativeAdData.getAdPatternType() == AdPatternType.NATIVE_1IMAGE_2TEXT
                || mNativeAdData.getAdPatternType() == AdPatternType.NATIVE_2IMAGE_2TEXT) {
            this.setAdImageMode(TTAdConstant.IMAGE_MODE_LARGE_IMG);
        } else if (mNativeAdData.getAdPatternType() == AdPatternType.NATIVE_3IMAGE) {
            this.setAdImageMode(TTAdConstant.IMAGE_MODE_GROUP_IMG);
        }

        if (mNativeAdData.isAppAd()) {
            this.setInteractionType(TTAdConstant.INTERACTION_TYPE_DOWNLOAD);
        } else {
            this.setInteractionType(TTAdConstant.INTERACTION_TYPE_LANDING_PAGE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG,"onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG,"onPause");
    }

    private void registerView(Context context, @NonNull ViewGroup container, List<View> clickViews, List<View> creativeViews, GMViewBinder viewBinder) {
        if (mNativeAdData != null && container instanceof TTNativeAdView) {
            TTNativeAdView nativeAdView = (TTNativeAdView) container;


            NativeAdContainer gdtNativeAdContainer;

            /**
             *
             * 防止gdtNativeAdContainer重复添加的问题
             * if true 表示 gdtNativeAdContainer已经添加到了nativeAdView ，就无须重复常见 ，但需要移除gdt往gdtNativeAdContainer添加的view(广点通的logo)
             * if false 表示 gdtNativeAdContainer没有添加到nativeAdView ，需要创建 gdtNativeAdContainer
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
            TTMediaView ttMediaView = nativeAdView.findViewById(viewBinder.mediaViewId);
            //绑定点击事件
            mNativeAdData.bindAdToView(context, gdtNativeAdContainer, null, clickViews, creativeViews);


            if (ttMediaView != null && getAdImageMode() == TTAdConstant.IMAGE_MODE_VIDEO) {
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
                    callDrawAdShow();
                    Log.d(TAG, "draw GDT --- onADExposed。。。。");
                }

                @Override
                public void onADClicked() {
                    Log.d(TAG, "draw GDT --- onADClicked。。。。");
                    callDrawAdClick();
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
    public void registerViewForInteraction(@NonNull Activity activity, @NonNull ViewGroup container, List<View> clickViews, List<View> creativeViews, GMViewBinder viewBinder) {
        super.registerViewForInteraction(activity, container, clickViews, creativeViews, viewBinder);
        registerView(activity, container, clickViews, creativeViews, viewBinder);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG,"onDestroy");
    }

    @Override
    public GMAdConstant.AdIsReadyStatus isReadyStatus() {
        if (mNativeAdData != null && mNativeAdData.isValid()) {
            return GMAdConstant.AdIsReadyStatus.AD_IS_READY;
        } else {
            return GMAdConstant.AdIsReadyStatus.AD_IS_NOT_READY;
        }
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
            callDrawVideoStart();
        }

        @Override
        public void onVideoPause() {
            callDrawVideoPause();
        }

        @Override
        public void onVideoResume() {
            callDrawVideoResume();
        }

        @Override
        public void onVideoCompleted() {
            callDrawVideoCompleted();
        }

        @Override
        public void onVideoError(com.qq.e.comm.util.AdError error) {
            callDrawVideoError(new GMCustomAdError(error.getErrorCode(), error.getErrorMsg()));
        }

        @Override
        public void onVideoStop() {
            Log.d(TAG, "onVideoStop");
        }

        @Override
        public void onVideoClicked() {
            Log.d(TAG, "onVideoClicked");
            callDrawAdClick();
        }
    };
}

