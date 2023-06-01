package com.ads.demo.custom.gdt;

import android.content.Context;

import com.ads.demo.util.ThreadUtils;
import com.bytedance.sdk.openadsdk.mediation.bridge.custom.MediationCustomInitLoader;
import com.bytedance.sdk.openadsdk.mediation.custom.MediationCustomInitConfig;
import com.qq.e.comm.managers.GDTAdSdk;
import com.qq.e.comm.managers.setting.GlobalSetting;
import com.qq.e.comm.managers.status.SDKStatus;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * YLH 自定义初始化类
 */
public class GdtCustomerConfig extends MediationCustomInitLoader {

    private static final String TAG = GdtCustomerConfig.class.getSimpleName();

    @Override
    public String getNetworkSdkVersion() {
        return SDKStatus.getIntegrationSDKVersion();
    }


    @Override
    public void initializeADN(Context context, MediationCustomInitConfig mediationCustomInitConfig, Map<String, Object> map) {
        /**
         * 在子线程中进行初始化
         */
        ThreadUtils.runOnThreadPool(new Runnable() {
            @Override
            public void run() {
                GDTAdSdk.init(context, mediationCustomInitConfig.getAppId());
                GlobalSetting.setPersonalizedState(0);//优量汇个性化推荐广告开关，0为开启个性化推荐广告，1为屏蔽个性化推荐广告。建议打开，提升广告收益
                //初始化成功回调
                callInitSuccess();
            }
        });
    }

    @Override
    public String getBiddingToken(Context context, Map<String, Object> extra) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                return GDTAdSdk.getGDTAdManger().getBuyerId(null); //开发者可不传;
            }
        });
        try {
            return future.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String getSdkInfo(Context context, Map<String, Object> extra) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<String> future = executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                String posId = (String) extra.get("slot_id");
                return GDTAdSdk.getGDTAdManger().getSDKInfo(posId);
            }
        });
        try {
            return future.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "";
    }

}
