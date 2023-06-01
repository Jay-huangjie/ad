package com.ads.demo.custom.beizi;

import android.content.Context;
import android.os.Handler;

import com.ads.demo.custom.beizi.util.ThreadUtils;
import com.beizi.fusion.BeiZiCustomController;
import com.beizi.fusion.BeiZis;
import com.bytedance.sdk.openadsdk.mediation.bridge.custom.MediationCustomInitLoader;
import com.bytedance.sdk.openadsdk.mediation.custom.MediationCustomInitConfig;

import java.util.Map;

/**
 * YLH 自定义初始化类
 */
public class BeiziCustomerConfig extends MediationCustomInitLoader {

    private static final String TAG = BeiziCustomerConfig.class.getSimpleName();

    @Override
    public void initializeADN(Context context, MediationCustomInitConfig mediationCustomInitConfig, Map<String, Object> map) {
        ThreadUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                BeiZis.init(context, mediationCustomInitConfig.getAppId(), new BeiZiCustomController() {
                    /**
                     * 是否允许SDK主动使用地理位置信息
                     *
                     * @return true可以获取，false禁止获取。默认为true
                     */
                    @Override
                    public boolean isCanUseLocation() {
                        return true;
                    }

                    /**
                     * 是否允许SDK主动使用ACCESS_WIFI_STATE权限
                     *
                     * @return true可以使用，false禁止使用。默认为true
                     */
                    @Override
                    public boolean isCanUseWifiState() {
                        return true;
                    }

                    /**
                     * 是否允许SDK主动使用手机硬件参数，如：imei，imsi
                     *
                     * @return true可以使用，false禁止使用。默认为true
                     */
                    @Override
                    public boolean isCanUsePhoneState() {
                        return true;
                    }

                    /**
                     * 是否能使用Oaid
                     *
                     * @return true可以使用，false禁止使用。默认为true
                     */
                    @Override
                    public boolean isCanUseOaid() {
                        return true;
                    }

                    /**
                     * 是否能使用Gaid
                     *
                     * @return true可以使用，false禁止使用。默认为true
                     */
                    @Override
                    public boolean isCanUseGaid() {
                        return true;
                    }
                });
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        callInitSuccess();
                    }
                },1000);
            }
        });
    }

    @Override
    public String getNetworkSdkVersion() {
        return BeiZis.getSdkVersion();
    }

}
