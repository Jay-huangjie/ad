package com.ads.demo.custom.gdt;

import android.widget.FrameLayout;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.mediation.MediationConstant;
import com.qq.e.ads.cfg.VideoOption;

import java.util.Map;

public class GdtUtils {
    public static VideoOption getGMVideoOption(AdSlot adSlot) {
/*        注：
        1:如下所示 如果需要"gdt_auto_play_policy"、"gdt_auto_play_muted"等自定义相关参数需要再加载广告的时候传入
        加载广告的时候传入的示例如下 (各个广告类型都适配) 通过MediationAdSlot.Builder().setExtraObject传入自定义参数

        val adslot = AdSlot.Builder()
                .setMediationAdSlot(
                        MediationAdSlot.Builder()
                                .setExtraObject("gdt_auto_play_policy", 1)
                                .setExtraObject(""gdt_auto_play_muted"", true)
                                .build()
                )
                .build()
*/


        VideoOption.Builder builder = new VideoOption.Builder();
        if (adSlot != null && adSlot.getMediationAdSlot() != null && adSlot.getMediationAdSlot().getExtraObject() != null) {
            Map extra = adSlot.getMediationAdSlot().getExtraObject();
            if (extra.get("gdt_auto_play_policy") instanceof Integer) {
                builder.setAutoPlayPolicy((Integer) extra.get("gdt_auto_play_policy"));
            }

            if (extra.get("gdt_auto_play_muted") instanceof Boolean) {
                builder.setAutoPlayMuted((Boolean) extra.get("gdt_auto_play_muted"));
            }

            if (extra.get("gdt_detail_page_muted") instanceof Boolean) {
                builder.setDetailPageMuted((Boolean) extra.get("gdt_detail_page_muted"));
            }

            if (extra.get("gdt_enable_detail_page") instanceof Boolean) {
                builder.setEnableDetailPage((Boolean) extra.get("gdt_enable_detail_page"));
            }

            if (extra.get("gdt_enable_user_control") instanceof Boolean) {
                builder.setEnableUserControl((Boolean) extra.get("gdt_enable_user_control"));
            }
        }
        return builder.build();
    }


    public static FrameLayout.LayoutParams getNativeAdLogoParams(AdSlot adSlot) {
        if (adSlot == null) {
            return null;
        }

        if (adSlot.getMediationAdSlot() == null) {
            return null;
        }

        Map extra = adSlot.getMediationAdSlot().getExtraObject();
        if (extra != null) {
            Object o = extra.get(MediationConstant.KEY_GDT_NATIVE_LOGO_PARAMS);
            if (o instanceof FrameLayout.LayoutParams) {
                return (FrameLayout.LayoutParams) o;
            }
        }

        return null;
    }

    public static int getGDTMaxVideoDuration(AdSlot adSlot) {
        if (adSlot == null) {
            return 0;
        }

        if (adSlot.getMediationAdSlot() == null) {
            return 0;
        }

        Map extra = adSlot.getMediationAdSlot().getExtraObject();
        if (extra != null) {
            Object o = extra.get(MediationConstant.KEY_GDT_MAX_VIDEO_DURATION);
            if (o instanceof Integer) {
                return (Integer) o;
            }
        }
        return 0;
    }

    public static int getGDTMinVideoDuration(AdSlot adSlot) {
        if (adSlot == null) {
            return 0;
        }

        if (adSlot.getMediationAdSlot() == null) {
            return 0;
        }

        Map extra = adSlot.getMediationAdSlot().getExtraObject();
        if (extra != null) {
            Object o = extra.get(MediationConstant.KEY_GDT_MIN_VIDEO_DURATION);
            if (o instanceof Integer) {
                return (Integer) o;
            }
        }
        return 0;
    }


}
