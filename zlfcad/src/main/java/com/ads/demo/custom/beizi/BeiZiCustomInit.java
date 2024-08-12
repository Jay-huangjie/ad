package com.ads.demo.custom.beizi;

import android.content.Context;
import android.text.TextUtils;

import com.beizi.fusion.BeiZiCustomController;
import com.beizi.fusion.BeiZis;
import com.bytedance.sdk.openadsdk.mediation.bridge.custom.MediationCustomInitLoader;
import com.bytedance.sdk.openadsdk.mediation.custom.MediationCustomInitConfig;

import java.util.Map;

/**
 * BeiZi适配器自定义初始化类
 */
public class BeiZiCustomInit extends MediationCustomInitLoader {

    public static final int PERSIONALIZED_ALLOW_STATUS = 1;//允许个性化推荐
    public static final int PERSIONALIZED_LIMIT_STATUS = 2;//限制个性化推荐

    public static final String OAID_KEY = "oaid_key";
    public static final String PERSIONAL_AD_STATUS_KEY = "persional_ad_status_key";

    private int personalAdStatus = PERSIONALIZED_ALLOW_STATUS;
    private String oaid = "";

    @Override
    public void initializeADN(Context context,
                              MediationCustomInitConfig mediationCustomInitConfig, Map<String,
            Object> localMap) {

        if (mediationCustomInitConfig == null) {
            return;
        }

        if (localMap != null) {
            if (localMap.containsKey(OAID_KEY) && !TextUtils.isEmpty(localMap.get(OAID_KEY).toString())) {
                oaid = localMap.get(OAID_KEY).toString();
            }

            if (localMap.containsKey(PERSIONAL_AD_STATUS_KEY) && !TextUtils.isEmpty(localMap.get(PERSIONAL_AD_STATUS_KEY).toString())) {
                personalAdStatus = Integer.parseInt(localMap.get(PERSIONAL_AD_STATUS_KEY).toString());
            }
        }


        String appId = mediationCustomInitConfig.getAppId();
        if (TextUtils.isEmpty(appId)) {
            return;
        }
        if (isInit()) {
            callInitSuccess();
            return;
        }
        initSDK(context, appId);
        callInitSuccess();
    }


    public void initSDK(Context context, String appId) {
        if (personalAdStatus == PERSIONALIZED_LIMIT_STATUS) {
            if (TextUtils.isEmpty(oaid)) {
                BeiZis.init(context, appId);
            } else {
                BeiZis.init(context, appId, new BeiZiCustomController() {
                    @Override
                    public boolean isCanUseLocation() {
                        return super.isCanUseLocation();
                    }

                    @Override
                    public boolean isCanUseWifiState() {
                        return super.isCanUseWifiState();
                    }

                    @Override
                    public boolean isCanUsePhoneState() {
                        return super.isCanUsePhoneState();
                    }

                    @Override
                    public boolean isCanUseOaid() {
                        return super.isCanUseOaid();
                    }

                    @Override
                    public boolean isCanUseGaid() {
                        return super.isCanUseGaid();
                    }
                }, null, oaid);
            }
            BeiZis.setSupportPersonalized(false);
        } else {
            BeiZis.init(context, appId);
        }

    }

    @Override
    public String getNetworkSdkVersion() {
        return BeiZis.getSdkVersion();
    }

}
