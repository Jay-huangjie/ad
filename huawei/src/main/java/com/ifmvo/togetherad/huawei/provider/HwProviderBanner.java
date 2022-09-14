package com.ifmvo.togetherad.huawei.provider;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.huawei.appgallery.agd.agdpro.api.AdsContext;
import com.huawei.appgallery.agd.agdpro.api.DislikeClickListener;
import com.huawei.appgallery.agd.agdpro.api.ITemplateAd;
import com.huawei.appgallery.agd.agdpro.api.InteractionListener;
import com.huawei.appgallery.agd.agdpro.api.TemplateLoadListener;
import com.huawei.appgallery.agd.core.api.AdSlot;
import com.ifmvo.togetherad.core.listener.BannerListener;
import com.ifmvo.togetherad.huawei.TogetherAdHw;

import java.util.ArrayList;
import java.util.List;

/**
 * create by hj on 2022/9/8
 **/
public abstract class HwProviderBanner extends HwProviderSplash {

    private List<ITemplateAd> adList = new ArrayList<>();

    @Override
    public void showBannerAd(@NonNull FragmentActivity activity, @NonNull String adProviderType, @NonNull String alias, @NonNull ViewGroup container, @NonNull BannerListener listener) {
        callbackBannerStartRequest(adProviderType, alias, listener);
        AdsContext adsContext = new AdsContext(activity);
        AdSlot adSlot = new AdSlot.Builder()
                .slotId(TogetherAdHw.INSTANCE.getIdMapKs().get(alias))                          // 广告槽位ID
                .rotationTime(HuaweiProvider.Banner.INSTANCE.getRotationTime())                // 多条广告轮播切换时间
                .darkMode(HuaweiProvider.ADSlot.INSTANCE.getDarkMode())    // 深色模式开关
                .acceptedSize(HuaweiProvider.Banner.INSTANCE.getWidth(), HuaweiProvider.Banner.INSTANCE.getHeight())               // 期望模板广告View的Size
                .mediaExtra(getJsonMediaExtra())                    // 个性化参数，透明传递，服务端请求参数
                .orientation(HuaweiProvider.Banner.INSTANCE.getOrientation())       // 广告方向
                .build();
        adsContext.loadBannerAds(adSlot, new TemplateLoadListener() {
            @Override
            public void onError(int code, String message) {
                // 广告加载失败回调
                callbackBannerFailed(adProviderType, alias, listener, code, message);
            }

            @Override
            public void onAdLoad(List<ITemplateAd> ads) {
                if (ads != null && !ads.isEmpty()) {
                    adList = ads;
                    ITemplateAd ad = ads.get(0);
                    callbackBannerLoaded(adProviderType, alias, listener);
                    ad.setInteractionListener(new InteractionListener() {
                        @Override
                        public void onAdClicked(View view) {
                            callbackBannerClicked(adProviderType, listener);
                        }

                        @Override
                        public void onAdShow(View view) {
                            callbackBannerExpose(adProviderType, listener);
                        }

                        @Override
                        public void onRenderFail(View view, int i, String s) {
                            destroyBannerAd();
                            callbackBannerFailed(adProviderType, alias, listener, i, s);
                        }

                        @Override
                        public void onRenderSuccess(View view, float v, float v1) {
                            if (view != null) {
                                container.addView(view);
                            }
                        }
                    });
                    ad.setDislikeClickListener(new DislikeClickListener() {
                        @Override
                        public void onDislikeClick() {
                            callbackBannerClosed(adProviderType, listener);
                        }
                    });
                    ad.render();
                }
            }
        });
    }

    @Override
    public void destroyBannerAd() {
        if (!adList.isEmpty()) {
            if (adList.get(0) != null) {
                adList.get(0).destroy();
                adList.clear();
            }
        }
    }
}
