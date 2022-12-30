package com.ads.demo.holder;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.provider.Settings;

import androidx.annotation.NonNull;

import com.ads.demo.BuildConfig;
import com.bytedance.msdk.api.UserInfoForSegment;
import com.bytedance.msdk.api.v2.GMAdConfig;
import com.bytedance.msdk.api.v2.GMAdConstant;
import com.bytedance.msdk.api.v2.GMConfigUserInfoForSegment;
import com.bytedance.msdk.api.v2.GMGdtOption;
import com.bytedance.msdk.api.v2.GMMediationAdSdk;
import com.bytedance.msdk.api.v2.GMPangleOption;
import com.bytedance.msdk.api.v2.GMPrivacyConfig;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * 可以用一个单例来保存TTAdManager实例，在需要初始化sdk的时候调用
 */
public class GMAdManagerHolder {

    private static boolean sInit;

    public static void init(Context context, String appID) {
        doInit(context, appID);
    }

    //step1:接入网盟广告sdk的初始化操作，详情见接入文档和穿山甲平台说明
    private static void doInit(@NonNull Context context, String appID) {
        if (!sInit) {
            GMMediationAdSdk.initialize(context, buildV2Config(context, appID));
            sInit = true;
        }
    }

    public static GMAdConfig buildV2Config(Context context, String appID) {
        /**
         * GMConfigUserInfoForSegment设置流量分组的信息
         * 注意：
         * 1、请每次都传入新的info对象
         * 2、字符串类型的值只能是大小写字母，数字，下划线，连字符，字符个数100以内 ( [A-Za-z0-9-_]{1,100} ) ，不符合规则的信息将被过滤掉，不起作用。
         */
        GMConfigUserInfoForSegment userInfo = new GMConfigUserInfoForSegment();
        userInfo.setUserId("msdk-demo");
        userInfo.setGender(UserInfoForSegment.GENDER_MALE);
        userInfo.setChannel("msdk-channel");
        userInfo.setSubChannel("msdk-sub-channel");
        userInfo.setAge(999);
        userInfo.setUserValueGroup("msdk-demo-user-value-group");

        return new GMAdConfig.Builder()
                .setAppId(appID)
                .setAppName(getAppName(context))
                .setDebug(BuildConfig.DEBUG)
                .setPublisherDid(getAndroidId(context))
                .setOpenAdnTest(false)
                .setConfigUserInfoForSegment(userInfo)
                .setPangleOption(new GMPangleOption.Builder()
                        .setIsPaid(false)
                        .setTitleBarTheme(GMAdConstant.TITLE_BAR_THEME_DARK)
                        .setAllowShowNotify(true)
                        .setAllowShowPageWhenScreenLock(true)
                        .setDirectDownloadNetworkType(GMAdConstant.NETWORK_STATE_WIFI, GMAdConstant.NETWORK_STATE_3G)
                        .setIsUseTextureView(true)
                        .setNeedClearTaskReset()
                        .setKeywords("")
                        .build())
                .setGdtOption(new GMGdtOption.Builder()
                        .setWxInstalled(false)
                        .setOpensdkVer(null)
                        .setSupportH265(false)
                        .setSupportSplashZoomout(false)
                        .build())
                /**
                 * 隐私协议设置，详见GMPrivacyConfig
                 */
                .setPrivacyConfig(new GMPrivacyConfig() {
                    // 重写相应的函数，设置需要设置的权限开关，不重写的将采用默认值
                    // 例如，重写isCanUsePhoneState函数返回true，表示允许使用ReadPhoneState权限。
                    @Override
                    public boolean isCanUsePhoneState() {
                        return true;
                    }

                    //当isCanUseWifiState=false时，可传入Mac地址信息，穿山甲sdk使用您传入的Mac地址信息
                    @Override
                    public String getMacAddress() {
                        return "";
                    }

                    // 设置青少年合规，默认值GMAdConstant.ADULT_STATE.AGE_ADULT为成年人
                    @Override
                    public GMAdConstant.ADULT_STATE getAgeGroup() {
                        return GMAdConstant.ADULT_STATE.AGE_ADULT;
                    }
                })
                .build();
    }

    public static String getAppName(Context context) {
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

    public static String getAndroidId(Context context) {
        String androidId = null;
        try {
            androidId = Settings.System.getString(context.getContentResolver(), Settings.System.ANDROID_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return androidId;
    }

    public static String getJson(String fileName, Context context) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            InputStream is = context.getAssets().open(fileName);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

}
