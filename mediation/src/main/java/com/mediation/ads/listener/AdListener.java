package com.mediation.ads.listener;

/**
 * created by hj on 2023/6/2.
 */
public interface AdListener extends BaseAdListener{
    /**
     * 请求到了广告
     */
    void onAdLoaded();

    /**
     * 广告被点击了
     */
    void onAdClicked();

    /**
     * 广告曝光了
     */
    void onAdExposure();

    /**
     * 广告消失了（ 点击跳过或者倒计时结束 ）
     */
    void onAdDismissed();

}
