package com.ads.demo.custom.beizi;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

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
        if (mediationCustomInitConfig == null) {
            return;
        }
        ThreadUtils.runOnUIThread(new Runnable() {
            @Override
            public void run() {
                BeiZis.init(context, mediationCustomInitConfig.getAppId());
                callInitSuccess();
            }
        });

    }

    @Override
    public String getNetworkSdkVersion() {
        return BeiZis.getSdkVersion();
    }

}
