package com.mediation.ads;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.mediation.init.MediationConfig;
import com.bytedance.sdk.openadsdk.mediation.init.MediationConfigUserInfoForSegment;

import java.util.ArrayList;
import java.util.List;


/**
 * 聚合广告初始化
 */
public class AdInitManager {

    private static boolean sInit;

    public static boolean initSuccess;

    private static List<InitCallback> callbacks;

    private final static Handler mHandler = new Handler(Looper.getMainLooper());

    public static void init(Context context, String appID) {
        init(context, appID, getUserInfoForSegment());
    }

    public static void init(Context context, String appID,
                            MediationConfigUserInfoForSegment mediationConfigUserInfoForSegment) {
        doInit(context, appID, mediationConfigUserInfoForSegment);
    }

    private static void doInit(@NonNull Context context, String appID, MediationConfigUserInfoForSegment info) {
        if (!sInit) {
            TTAdSdk.init(context, buildConfig(context, appID, info));
            TTAdSdk.start(new TTAdSdk.Callback() {
                @Override
                public void success() {
                    initSuccess = true;
                    mHandler.post(() -> {
                        if (callbacks != null) {
                            for (InitCallback c : callbacks) {
                                c.onSuccess();
                            }
                        }
                    });
                }

                @Override
                public void fail(int i, String s) {
                    initSuccess = false;
                    mHandler.post(() -> {
                        if (callbacks != null) {
                            for (InitCallback c : callbacks) {
                                c.onFair();
                            }
                        }
                    });
                }
            });
            sInit = true;
        }
    }

    private static TTAdConfig buildConfig(Context context, String appID, MediationConfigUserInfoForSegment info) {
        return new TTAdConfig.Builder()
                .appId(appID)
                .appName(getAppName(context))
                .debug(BuildConfig.DEBUG)
                .useMediation(true)
                .setMediationConfig(new MediationConfig.Builder() //可设置聚合特有参数详细设置请参考该api
                        .setMediationConfigUserInfoForSegment(info)//如果您需要配置流量分组信息请参考该api
                        .build())
                .build();
    }

    //监听sdk是否初始化成功
    public static void addInitCallback(InitCallback callback) {
        if (callbacks == null) {
            callbacks = new ArrayList<>();
        }
        callbacks.add(callback);
    }

    public static void removeInitCallback(InitCallback callback) {
        if (callbacks == null) {
            callbacks = new ArrayList<>();
        }
        callbacks.remove(callback);
    }


    private static MediationConfigUserInfoForSegment getUserInfoForSegment() {
        MediationConfigUserInfoForSegment userInfo = new MediationConfigUserInfoForSegment();
        userInfo.setUserId("msdk-demo");
        userInfo.setGender(MediationConfigUserInfoForSegment.GENDER_MALE);
        userInfo.setChannel("msdk-channel");
        userInfo.setSubChannel("msdk-sub-channel");
        userInfo.setAge(20);
        userInfo.setUserValueGroup("msdk-demo-user-value-group");
        return userInfo;
    }


    private static String getAppName(Context context) {
        String name = "";
        PackageManager packageManager = context.getPackageManager();
        ApplicationInfo applicationInfo = null;
        try {
            applicationInfo = packageManager.getApplicationInfo(context.getApplicationInfo().packageName, 0);
            name = (String) (applicationInfo != null ? packageManager.getApplicationLabel(applicationInfo) : name);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return name;
    }

    public interface InitCallback {
        void onSuccess();

        void onFair();
    }

}
