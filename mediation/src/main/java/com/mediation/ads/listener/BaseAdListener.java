package com.mediation.ads.listener;

/**
 * create by hj on 2023/1/6
 **/
interface BaseAdListener {
    /**
     * 广告加载失败
     *
     * @param errorCode
     * @param failedMsg
     */
    void onAdFailed(int errorCode, String failedMsg);
}
