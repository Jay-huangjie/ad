package com.mediation.ads.provide;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.mediation.ads.UIUtils;
import com.mediation.ads.listener.AdListener;
import com.mediation.ads.listener.IAdSlotBuild;

import java.util.List;

/**
 * created by hj on 2023/6/2.
 */
public class AdBannerProvide extends BaseProvide {

    private AdListener mListener;

    private ViewGroup adContain;

    private TTNativeExpressAd bannerAd;

    private int width, height;

    public AdBannerProvide(Activity mActivity, ViewGroup adContain, AdListener adListener) {
        this(mActivity, adContain, UIUtils.getScreenWidthInPx(mActivity), UIUtils.dp2px(mActivity, 150f), adListener);
    }

    public AdBannerProvide(Activity mActivity, ViewGroup adContain,
                           int width, int height, AdListener adListener) {
        super(mActivity);
        this.adContain = adContain;
        this.mListener = adListener;
        this.width = width;
        this.height = height;
    }


    @Override
    protected void init(String id, IAdSlotBuild iAdSlotBuild) {
        adContain.removeAllViews();
        AdSlot.Builder builder = new AdSlot.Builder()
                .setCodeId(id)
                .setImageAcceptedSize(width, height);// 单位px
        if (iAdSlotBuild != null) {
            iAdSlotBuild.getBuild(builder);
        }
        AdSlot adSlot = builder.build();
        TTAdNative adNativeLoader = TTAdSdk.getAdManager().createAdNative(mActivity);
        adNativeLoader.loadBannerExpressAd(adSlot, new TTAdNative.NativeExpressAdListener() {
            @Override
            public void onError(int i, String s) {
                mListener.onAdFailed(i, s);
            }

            @Override
            public void onNativeExpressAdLoad(List<TTNativeExpressAd> list) {
                mListener.onAdLoaded();
                if (list.size() > 0) {
                    bannerAd = list.get(0);
                    if (bannerAd != null) {
                        bannerAd.setExpressInteractionListener(new TTNativeExpressAd.ExpressAdInteractionListener() {
                            @Override
                            public void onAdClicked(View view, int i) {
                                mListener.onAdClicked();
                            }

                            @Override
                            public void onAdShow(View view, int i) {
                                mListener.onAdExposure();
                            }

                            @Override
                            public void onRenderFail(View view, String s, int i) {
                                mListener.onAdFailed(-1, "onRenderFail");
                            }

                            @Override
                            public void onRenderSuccess(View view, float v, float v1) {

                            }
                        });
                        bannerAd.setDislikeCallback(mActivity, new TTAdDislike.DislikeInteractionCallback() {
                            @Override
                            public void onShow() {

                            }

                            @Override
                            public void onSelected(int i, String s, boolean b) {
                                if (adContain != null) {
                                    adContain.removeAllViews();
                                }
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                        View expressAdView = bannerAd.getExpressAdView();
                        if (expressAdView != null) {
                            adContain.removeAllViews();
                            adContain.addView(expressAdView);
                        }
                    }
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        if (bannerAd != null) {
            bannerAd.destroy();
        }
    }

}
