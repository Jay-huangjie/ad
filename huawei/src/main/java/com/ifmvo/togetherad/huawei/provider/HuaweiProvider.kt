package com.ifmvo.togetherad.huawei.provider

import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.huawei.appgallery.agd.core.api.AgdAdConstant
import com.ifmvo.togetherad.core.listener.EyesSplashListener
import com.ifmvo.togetherad.core.listener.FullVideoListener
import com.ifmvo.togetherad.core.listener.NativeExpressListener


/**
 *  华为
 * Created by hj
 */
open class HuaweiProvider : HwProviderNative() {

    object Banner {
        // 多条广告轮播切换时间
        //取值范围：0或[30,120]
        var rotationTime = 0
        var width: Int = 0
        var height: Int = 0

        //广告方向
        var orientation: Int = AgdAdConstant.VERTICAL
    }

    object ADSlot {
        //深色模式开关
        var darkMode: Int = AgdAdConstant.UIMODE_DEFAULT
    }

    object Inter {
        //广告方向
        var orientation: Int = AgdAdConstant.VERTICAL
    }

    object Reward {
        //广告方向
        var orientation: Int = AgdAdConstant.VERTICAL

        //广告声音状态
        var soundStatus: Int = AgdAdConstant.SOUND_ON

        //协议版本号
        var version: Int = 0;
    }

    object Native {
        // 单次请求广告个数 [1,3]
        var adCount: Int = 1;

        var width: Int = 0
        var height: Int = 0
    }

    object Splash {
        /**
         * 期望模板广告View的Size。
        单位：dp
        “acceptedSize”参数控制View自适应逻辑如下：
        情况一：width<=0或height<=0
        如果请求参数width<=0，则请求到的View宽将自适应为广告卡片宽。
        如果请求参数height<=0，则请求到的View高将自适应为广告卡片高。
        情况二：0<width<ScreenWidth或0<height<ScreenHeight
        如果请求参数0<width<ScreenWidth，则请求到的View宽为请求参数width的值。
        如果请求参数0<height<ScreenHeight，则请求到的View高为请求参数height的值。
        情况三：width>=ScreenWidth或height>=ScreenHeight
        如果请求参数width>=ScreenWidth，则请求到的View宽为屏幕宽的值。
        如果请求参数height>=ScreenHeight，则请求到的View高为屏幕高的值。
         */
        var width: Int = 0;
        var height: Int = 0;

        // 禁用跳过按钮和禁用倒计时功能开关
        var disableSdkCountDown = false
    }

    override fun nativeAdIsBelongTheProvider(adObject: Any): Boolean {
        return false
    }

    override fun destroyNativeExpressAd(adObject: Any) {
    }

    override fun getNativeExpressAdList(
        activity: FragmentActivity,
        adProviderType: String,
        alias: String,
        adCount: Int,
        listener: NativeExpressListener
    ) {
    }

    override fun resumeNativeAd(adObject: Any) {
    }

    override fun pauseNativeAd(adObject: Any) {
    }

    override fun destroyNativeAd(adObject: Any) {
    }

    override fun requestFullVideoAd(
        activity: FragmentActivity,
        adProviderType: String,
        alias: String,
        listener: FullVideoListener
    ) {
    }

    override fun nativeExpressAdIsBelongTheProvider(adObject: Any): Boolean {
        return false
    }

    override fun showFullVideoAd(activity: FragmentActivity): Boolean {
        return false
    }
}