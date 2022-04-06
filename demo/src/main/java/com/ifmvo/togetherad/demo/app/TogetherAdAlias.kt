package com.ifmvo.togetherad.demo.app

/**
 * 所有广告位的别名
 *
 * 列举你项目中的所有广告位，并给每个广告位起个名字
 *
 * 用于初始化广告位ID 以及 广告的请求
 *
 * Created by Matthew Chen on 2020-04-16.
 */
object TogetherAdAlias {

    //开屏
    const val AD_SPLASH = "ad_splash"

    //原生模板 简单使用
    const val AD_NATIVE_EXPRESS_SIMPLE = "ad_native_express_simple"

    //原生模板 在 RecyclerView 中使用
    const val AD_NATIVE_EXPRESS_RECYCLERVIEW = "ad_native_express_recyclerview"

    //原生 简单使用
    const val AD_NATIVE_SIMPLE = "ad_native_simple"

    //原生 在 RecyclerView 中使用
    const val AD_NATIVE_RECYCLERVIEW = "ad_native_recyclerview"

    //Banner
    const val AD_BANNER = "ad_banner"

    //插屏广告
    const val AD_INTER = "ad_inter"

    //激励广告
    const val AD_REWARD = "ad_reward"

    //全屏视频广告
    const val AD_FULL_VIDEO = "ad_full_video"

    //开屏混合使用
    const val AD_HYBRID_SPLASH = "ad_splash_and_native"

    //原生模板混合
    const val AD_HYBRID_EXPRESS = "ad_hybrid_express"

    //原生模板混合
    const val AD_HYBRID_VERTICAL_PREMOVIE = "ad_hybrid_vertical_premovie"

}