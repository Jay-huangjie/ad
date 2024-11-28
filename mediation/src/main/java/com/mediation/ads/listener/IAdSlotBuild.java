package com.mediation.ads.listener;

import com.bytedance.sdk.openadsdk.AdSlot;

/**
 * created by hj on 2024/11/28.
 */
public interface IAdSlotBuild {

    AdSlot.Builder getBuild(AdSlot.Builder builder);

}
