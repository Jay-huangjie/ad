package com.zlfcapp.batterymanager.huawei.provider;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.huawei.appgallery.agd.agdpro.api.AdsContext;
import com.huawei.appgallery.agd.agdpro.api.DislikeClickListener;
import com.huawei.appgallery.agd.agdpro.api.IInterstitialAd;
import com.huawei.appgallery.agd.agdpro.api.ITemplateAd;
import com.huawei.appgallery.agd.agdpro.api.InteractionListener;
import com.huawei.appgallery.agd.agdpro.api.InterstitialInteractionListener;
import com.huawei.appgallery.agd.agdpro.api.InterstitialLoadListener;
import com.huawei.appgallery.agd.agdpro.api.TemplateLoadListener;
import com.huawei.appgallery.agd.core.api.AdSlot;
import com.huawei.appgallery.agd.core.api.AgdAdConstant;
import com.zlfcapp.batterymanager.core.listener.BannerListener;
import com.zlfcapp.batterymanager.core.listener.InterListener;
import com.zlfcapp.batterymanager.huawei.TogetherAdHw;

import java.util.ArrayList;
import java.util.List;

/**
 * create by hj on 2022/9/8
 **/
public abstract class HwProviderInter extends HwProviderBanner {

    private IInterstitialAd ad;

    @Override
    public void requestInterAd(@NonNull FragmentActivity activity, @NonNull String adProviderType, @NonNull String alias, @NonNull InterListener listener) {
        callbackInterStartRequest(adProviderType, alias, listener);
        AdsContext adsContext = new AdsContext(activity);
        AdSlot adSlot = new AdSlot.Builder()
                .slotId(TogetherAdHw.INSTANCE.getIdMapKs().get(alias))                          // 广告槽位ID
                .darkMode(HuaweiProvider.ADSlot.INSTANCE.getDarkMode())    // 深色模式开关
                .mediaExtra(getJsonMediaExtra())                    // 个性化参数，透明传递，服务端请求参数
                .orientation(HuaweiProvider.Inter.INSTANCE.getOrientation())       // 广告方向
                .build();
        adsContext.loadInterstitialAd(adSlot, new InterstitialLoadListener() {
            @Override
            public void onAdLoad(IInterstitialAd iInterstitialAd) {
                ad = iInterstitialAd;
                callbackInterLoaded(adProviderType, alias, listener);
                if (ad != null) {
                    ad.setInteractionListener(new InterstitialInteractionListener() {
                        @Override
                        public void onAdClicked() {
                            // 广告点击回调
                            callbackInterClicked(adProviderType, listener);
                        }

                        @Override
                        public void onAdShowError(int i) {
                            callbackInterFailed(adProviderType, alias, listener, i, getShowError(i));
                            destroyInterAd();
                        }

                        @Override
                        public void onAdClose() {
                            callbackInterClosed(adProviderType, listener);
                        }

                        @Override
                        public void onAdShow() {
                            // 广告展示成功回调
                            callbackInterExpose(adProviderType, listener);
                        }
                    });
                }
                showInterAd(activity);
            }

            @Override
            public void onError(int code, String message) {
                // 广告加载失败回调
                callbackInterFailed(adProviderType, alias, listener, code, message);
            }
        });
    }

    @Override
    public void showInterAd(@NonNull FragmentActivity activity) {
        if (ad != null) {
            ad.show(activity);
        }
    }

    @Override
    public void destroyInterAd() {
        if (ad != null) {
            ad.destroy();
            ad = null;
        }
    }
}
