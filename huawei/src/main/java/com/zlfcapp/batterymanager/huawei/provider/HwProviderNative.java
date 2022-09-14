package com.zlfcapp.batterymanager.huawei.provider;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.huawei.appgallery.agd.agdpro.api.AdsContext;
import com.huawei.appgallery.agd.agdpro.api.DislikeClickListener;
import com.huawei.appgallery.agd.agdpro.api.ITemplateAd;
import com.huawei.appgallery.agd.agdpro.api.InteractionListener;
import com.huawei.appgallery.agd.agdpro.api.TemplateLoadListener;
import com.huawei.appgallery.agd.core.api.AdSlot;
import com.huawei.appgallery.agd.core.api.AgdAdConstant;
import com.zlfcapp.batterymanager.core.listener.NativeListener;
import com.zlfcapp.batterymanager.huawei.TogetherAdHw;

import java.util.List;

/**
 * create by hj on 2022/9/9
 **/
public abstract class HwProviderNative extends HwProviderReward {

    @Override
    public void getNativeAdList(@NonNull FragmentActivity activity, @NonNull String adProviderType, @NonNull String alias, int maxCount, @NonNull NativeListener listener) {
        callbackNativeStartRequest(adProviderType, alias, listener);
        // 创建AdsContext对象需要传入Activity对象
        AdsContext adsContext = new AdsContext(activity);
        AdSlot adSlot = new AdSlot.Builder()
                .slotId(TogetherAdHw.INSTANCE.getIdMapKs().get(alias))
                .adCount(HuaweiProvider.Native.INSTANCE.getAdCount())
                .darkMode(HuaweiProvider.ADSlot.INSTANCE.getDarkMode())    // 深色模式开关
                .acceptedSize(HuaweiProvider.Native.INSTANCE.getWidth(), HuaweiProvider.Native.INSTANCE.getHeight())               // 期望模板广告View的Size
                .mediaExtra(getJsonMediaExtra())
                .build();
        adsContext.loadFeedAds(adSlot, new TemplateLoadListener() {
            @Override
            public void onError(int code, String message) {
                // 广告加载失败回调
                callbackNativeFailed(adProviderType, alias, listener, code, message);
            }

            @Override
            public void onAdLoad(List<ITemplateAd> ads) {
                //list是空的，按照错误来处理
                if (ads == null || ads.isEmpty()) {
                    callbackNativeFailed(adProviderType, alias, listener, null,
                            "请求成功，但是返回的list为空");
                    return;
                }
                callbackNativeLoaded(adProviderType, alias, listener, ads);
                ITemplateAd ad = ads.get(0);
            }
        });
    }
}
