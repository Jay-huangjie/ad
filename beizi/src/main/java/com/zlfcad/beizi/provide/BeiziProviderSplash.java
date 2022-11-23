package com.zlfcad.beizi.provide;

import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentActivity;

import com.beizi.fusion.AdListener;
import com.beizi.fusion.SplashAd;
import com.ifmvo.togetherad.core.listener.SplashListener;
import com.ifmvo.togetherad.core.provider.BaseAdProvider;
import com.zlfcad.beizi.TogetherAdBeizi;

/**
 * create by hj on 2022/11/22
 **/
public abstract class BeiziProviderSplash extends BaseAdProvider {

    private SplashAd splashAd;

    @Override
    public void loadOnlySplashAd(FragmentActivity activity, String adProviderType,
                                 String alias, SplashListener listener) {

    }

    @Override
    public void loadAndShowSplashAd(FragmentActivity activity, String adProviderType,
                                    String alias, ViewGroup container, SplashListener listener) {
//1、创建开屏广告（假如支持开屏点睛，自定义跳过按钮skipView必须传null）
        callbackSplashStartRequest(adProviderType, alias, listener);
        splashAd = new SplashAd(activity, null, TogetherAdBeizi.getInstance()
                .getIdMaps().get(alias), new AdListener() {
            @Override
            public void onAdLoaded() {
                callbackSplashLoaded(adProviderType, alias, listener);
                if (splashAd != null) {
                    splashAd.show(container);
                }
            }

            @Override
            public void onAdShown() {
                callbackSplashExposure(adProviderType, listener);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                callbackSplashFailed(adProviderType, alias, listener, errorCode, "");
            }

            @Override
            public void onAdClosed() {
                callbackSplashDismiss(adProviderType, listener);
            }

            @Override
            public void onAdTick(long millisUnitFinished) {
//                Log.i("BeiZisDemo", "onAdTick millisUnitFinished == " + millisUnitFinished);
            }

            @Override
            public void onAdClicked() {
                callbackSplashClicked(adProviderType, listener);
            }
        }, BeiziProvider.Splash.mTotalTime);
        int width = BeiziProvider.Splash.mWidth;
        int height = BeiziProvider.Splash.mHeight;
        if (width == 0 || height == 0) {
            callbackSplashFailed(adProviderType, alias, listener, -100, "请设置闪屏页的宽高");
            return;
        }
        splashAd.loadAd(width, height);
        if (BeiziProvider.Splash.isSupportSplashClickEye) {
            //2、设置开屏点睛监听器
            splashAd.setSplashClickEyeListener(new SplashAd.SplashClickEyeListener() {
                @Override
                public void onSplashClickEyeAnimationFinish() {
                }

                @Override
                public void isSupportSplashClickEye(boolean isSupport) {
                    BeiziProvider.Splash.isSupportSplashClickEye = isSupport;
                }
            });
        }
    }

    @Override
    public void destroySplashAd(Context context) {
        if (splashAd != null) {
            splashAd.cancel(context);
        }
    }
}
