package com.ifmvo.togetherad.core.provider

import android.app.Activity
import android.view.ViewGroup
import com.ifmvo.togetherad.core.listener.*
import org.jetbrains.annotations.NotNull

/* 
 * Created by Matthew Chen on 2020-04-03.
 */
interface IAdProvider {

    /**
     * -----------------------------------开屏广告-----------------------------------
     */
    fun loadAndShowSplashAd(
            @NotNull activity: Activity,//由于百度必须使用 Activity，所以这里统一传
            @NotNull adProviderType: String,
            @NotNull alias: String,//当前广告位的别名
            @NotNull container: ViewGroup,//开屏广告的容器，开屏广告请求到之后会自动添加进去
            @NotNull listener: SplashListener//回调
    )

    //开屏广告仅请求
    fun loadOnlySplashAd(
            @NotNull activity: Activity,//由于百度必须使用 Activity，所以这里统一传
            @NotNull adProviderType: String,
            @NotNull alias: String,//当前广告位的别名
            @NotNull listener: SplashListener//回调
    )

    //展示开屏广告
    fun showSplashAd(
            @NotNull container: ViewGroup
    ): Boolean

    /**
     * -----------------------------------Banner 广告-----------------------------------
     */
    fun showBannerAd(
            @NotNull activity: Activity,
            @NotNull adProviderType: String,
            @NotNull alias: String,
            @NotNull container: ViewGroup,
            @NotNull listener: BannerListener
    )

    //销毁 Banner 广告
    fun destroyBannerAd()

    /**
     * -----------------------------------插屏-----------------------------------
     */
    fun requestInterAd(
            @NotNull activity: Activity,
            @NotNull adProviderType: String,
            @NotNull alias: String,
            @NotNull listener: InterListener
    )

    //展示插屏广告
    fun showInterAd(
            @NotNull activity: Activity
    )

    //销毁插屏广告
    fun destroyInterAd()

    /**
     * -----------------------------------获取自渲染信息流-----------------------------------
     */
    fun getNativeAdList(
            @NotNull activity: Activity,
            @NotNull adProviderType: String,
            @NotNull alias: String,
            maxCount: Int,
            @NotNull listener: NativeListener
    )

    //判断原生广告对象是否属于这个提供商
    fun nativeAdIsBelongTheProvider(@NotNull adObject: Any): Boolean

    //控制原生自渲染的生命周期
    fun resumeNativeAd(@NotNull adObject: Any)

    //控制原生自渲染的生命周期
    fun pauseNativeAd(@NotNull adObject: Any)

    //控制原生自渲染的生命周期
    fun destroyNativeAd(@NotNull adObject: Any)

    /**
     * -----------------------------------获取原生模板列表-----------------------------------
     */
    fun getNativeExpressAdList(
            @NotNull activity: Activity,
            @NotNull adProviderType: String,
            @NotNull alias: String,
            adCount: Int,
            @NotNull listener: NativeExpressListener
    )

    //控制原生模板的生命周期
    fun destroyNativeExpressAd(adObject: Any)

    //判断模板广告对象是否属于这个提供商
    fun nativeExpressAdIsBelongTheProvider(adObject: Any): Boolean

    /**
     * -----------------------------------请求激励广告 -----------------------------------
     */
    fun requestAndShowRewardAd(
            @NotNull activity: Activity,
            @NotNull adProviderType: String,
            @NotNull alias: String,
            @NotNull listener: RewardListener
    )

    fun requestRewardAd(
            @NotNull activity: Activity,
            @NotNull adProviderType: String,
            @NotNull alias: String,
            @NotNull listener: RewardListener
    )

    //展示激励广告
    fun showRewardAd(
            @NotNull activity: Activity
    ): Boolean

    /**
     * -----------------------------------请求全屏视频广告 -----------------------------------
     */
    fun requestFullVideoAd(
            @NotNull activity: Activity,
            @NotNull adProviderType: String,
            @NotNull alias: String,
            @NotNull listener: FullVideoListener
    )

    fun showFullVideoAd(
            @NotNull activity: Activity
    ): Boolean
}