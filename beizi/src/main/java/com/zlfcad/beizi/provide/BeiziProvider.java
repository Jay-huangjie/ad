package com.zlfcad.beizi.provide;

import android.view.ViewGroup;

import androidx.fragment.app.FragmentActivity;

import com.ifmvo.togetherad.core.listener.BannerListener;
import com.ifmvo.togetherad.core.listener.EyesSplashListener;
import com.ifmvo.togetherad.core.listener.FullVideoListener;
import com.ifmvo.togetherad.core.listener.InterListener;
import com.ifmvo.togetherad.core.listener.NativeExpressListener;
import com.ifmvo.togetherad.core.listener.NativeListener;
import com.ifmvo.togetherad.core.listener.RewardListener;

/**
 * create by hj on 2022/11/22
 **/
public class BeiziProvider extends BeiziProviderSplash{

    public BeiziProvider() {
    }

    public static class Splash{
        //超时时间
        public static long mTotalTime = 5000;
        public static int mWidth = 0;
        public static int mHeight = 0;
        //是否支持点睛
        public static boolean isSupportSplashClickEye = false;
    }

    @Override
    public void loadSplashEye(FragmentActivity activity, String adProviderType, String alias, ViewGroup container, EyesSplashListener listener) {

    }

    @Override
    public void loadMainSplashEye(FragmentActivity activity, EyesSplashListener listener) {

    }

    @Override
    public boolean showSplashAd(ViewGroup container) {
        return false;
    }

    @Override
    public void showBannerAd(FragmentActivity activity, String adProviderType, String alias, ViewGroup container, BannerListener listener) {

    }

    @Override
    public void destroyBannerAd() {

    }

    @Override
    public void requestInterAd(FragmentActivity activity, String adProviderType, String alias, InterListener listener) {

    }

    @Override
    public void showInterAd(FragmentActivity activity) {

    }

    @Override
    public void destroyInterAd() {

    }

    @Override
    public void getNativeAdList(FragmentActivity activity, String adProviderType, String alias, int maxCount, NativeListener listener) {

    }

    @Override
    public boolean nativeAdIsBelongTheProvider(Object adObject) {
        return false;
    }

    @Override
    public void resumeNativeAd(Object adObject) {

    }

    @Override
    public void pauseNativeAd(Object adObject) {

    }

    @Override
    public void destroyNativeAd(Object adObject) {

    }

    @Override
    public void getNativeExpressAdList(FragmentActivity activity, String adProviderType, String alias, int adCount, NativeExpressListener listener) {

    }

    @Override
    public void destroyNativeExpressAd(Object adObject) {

    }

    @Override
    public boolean nativeExpressAdIsBelongTheProvider(Object adObject) {
        return false;
    }

    @Override
    public void requestAndShowRewardAd(FragmentActivity activity, String adProviderType, String alias, RewardListener listener) {

    }

    @Override
    public void requestRewardAd(FragmentActivity activity, String adProviderType, String alias, RewardListener listener) {

    }

    @Override
    public boolean showRewardAd(FragmentActivity activity) {
        return false;
    }

    @Override
    public void requestFullVideoAd(FragmentActivity activity, String adProviderType, String alias, FullVideoListener listener) {

    }

    @Override
    public boolean showFullVideoAd(FragmentActivity activity) {
        return false;
    }
}
