package com.ifmvo.togetherad.core

import com.ifmvo.togetherad.core.custom.native_.imageloader.AdImageLoader
import com.ifmvo.togetherad.core.custom.native_.imageloader.DefaultImageLoader
import com.ifmvo.togetherad.core.entity.AdProviderEntity
import com.ifmvo.togetherad.core.listener.AllAdListener
import com.ifmvo.togetherad.core.utils.logi
import org.jetbrains.annotations.NotNull

/**
 * Created by Matthew Chen on 2020-04-02.
 */
object TogetherAd {

    /**
     * 自定义公共的的的广告提供商比例，有序
     */
    private val mRatioPublicMap = linkedMapOf<String, Int>()

    /**
     * 保存每个广告位的分发方式
     */
    var mDispatchTypeMap = hashMapOf<String, DispatchType>()

    /**
     * 所有注册的广告提供商
     */
    var mProviders = mutableMapOf<String, AdProviderEntity>()
        private set

    /**
     * 添加广告提供商
     */
    fun addProvider(@NotNull adProviderEntity: AdProviderEntity) {
        mProviders[adProviderEntity.providerType] = adProviderEntity
        "注册广告提供商：${adProviderEntity.providerType}".logi()
    }

    internal fun getProvider(providerType: String): AdProviderEntity? {
        return mProviders[providerType]
    }

    /**
     * 全局配置默认比例
     *
     * ratioMap:
     * val map = LinkedHashLinkedHashMap<String, Int>()
     * map.put(AdProviderType.GDT, 1)
     * map.put(AdProviderType.CSJ, 1)
     * map.put(AdProviderType.BAIDU, 2)
     */
    fun setPublicProviderRatio(@NotNull ratioMap: LinkedHashMap<String, Int>) {
        val ratio = StringBuilder()
        ratioMap.entries.forEach {
            ratio.append("${it.key}:${it.value}")
            ratio.append(",")
        }
        "设置默认广告提供商比例：$ratio".logi()

        mRatioPublicMap.clear()
        mRatioPublicMap.putAll(ratioMap)
    }

    /**
     * 有自定义的就用自定义的，没有自定义就每个注册的广告商等比
     */
    fun getPublicProviderRatio(): LinkedHashMap<String, Int> {
        return if (mRatioPublicMap.isNotEmpty()) {
            mRatioPublicMap
        } else {
            val defaultMap = linkedMapOf<String, Int>()
            mProviders.entries.forEach {
                defaultMap[it.key] = 0 //所有注册的广告商权重都是 0
            }
            defaultMap
        }
    }

    /**
     * 可自定义图片加载处理
     */
    var mImageLoader: AdImageLoader? = DefaultImageLoader()
        private set

    /**
     * 提供了自定义图片加载框架的接口
     */
    fun setCustomImageLoader(@NotNull imageLoader: AdImageLoader) {
        mImageLoader = imageLoader
    }

    /**
     * 是否打印 Log 日志
     */
    var printLogEnable: Boolean = true

    /**
     * 是否失败切换 （ 当请求广告失败时，是否允许切换到其他广告提供商再次请求 ）
     */
    var failedSwitchEnable: Boolean = true

    /**
     * 通用最大拉取延时时间ms（ 请求广告的超时时间；3000 ≤ value ≥ 10000 ）
     */
    var maxFetchDelay: Long = 0
        set(value) {
            field = when {
                value < 3000 -> {
                    3000
                }
                value > 10000 -> {
                    10000
                }
                else -> {
                    value
                }
            }
        }

    /**
     * 所有广告商所有广告类型的广告都会回调这个监听器
     * 主要是方便做统计：请求成功率、请求失败信息等
     */
    var allAdListener: AllAdListener? = null

    /**
     * 分发方式
     * DispatchType.Random 按照权重比例随机分发
     * DispatchType.Priority 按照优先级分发广告
     */
    var dispatchType: DispatchType = DispatchType.Random
}