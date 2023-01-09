package com.zlfcapp.zlfcad;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import com.beizi.fusion.BeiZiCustomController;
import com.beizi.fusion.BeiZis;
import com.bytedance.msdk.api.v2.GMAdConfig;
import com.bytedance.msdk.api.v2.GMAdConstant;
import com.bytedance.msdk.api.v2.GMGdtOption;
import com.bytedance.msdk.api.v2.GMMediationAdSdk;
import com.bytedance.msdk.api.v2.GMPangleOption;
import com.bytedance.msdk.api.v2.GMPrivacyConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * create by hj on 2023/1/6
 **/
public class AdCustomManager {

    private static boolean adOpen = false;
    private static boolean sInit;
    private static AdCustomConfig config;
    //groMore是否初始化
    private static boolean groMoreInit = false;

    public static void setAdOpen(boolean adOpen) {
        AdCustomManager.adOpen = adOpen;
    }

    public static boolean isAdOpen() {
        return adOpen;
    }

    public static void initAd(Context context, boolean isOpenAd, AdCustomConfig config) {
        AdCustomManager.adOpen = isOpenAd;
        AdCustomManager.config = config;
        doInit(context);
    }

    public static AdCustomConfig getConfig() {
        return config;
    }

    private static void doInit(@NonNull Context context) {
        if (!sInit) {
            sInit = true;
            if (AdCustomManager.adOpen) {
                initGroMore(context);
            } else {
                initBeizi(context);
            }
        }
    }


    public static void initBeizi(Context context) {
        BeiZis.init(context, config.getBzAppId(), new BeiZiCustomController() {
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
    }

    public static void initGroMore(Context context) {
        if (!groMoreInit) {
            GMMediationAdSdk.initialize(context, buildV2Config(context));
            groMoreInit = true;
        }
    }

    private static GMAdConfig buildV2Config(Context context) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(getJson("androidlocalconfig.json", context));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return new GMAdConfig.Builder()
                .setAppId(config.getGroMoreAppId())
                .setAppName(getAppName(context))
                .setDebug(BuildConfig.DEBUG)
                .setPublisherDid(config.getPublisherDid())
                .setOpenAdnTest(false)
                .setConfigUserInfoForSegment(config.getUserInfo())
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
                .setCustomLocalConfig(jsonObject)
                .build();
    }


    private static String getJson(String fileName, Context context) {
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

}
