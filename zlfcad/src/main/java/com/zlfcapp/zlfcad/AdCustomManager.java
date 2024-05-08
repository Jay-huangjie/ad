package com.zlfcapp.zlfcad;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.beizi.fusion.BeiZiCustomController;
import com.beizi.fusion.BeiZis;
import com.bytedance.sdk.openadsdk.TTAdConfig;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.bytedance.sdk.openadsdk.mediation.init.MediationConfig;
import com.mediation.ads.AdInitManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * create by hj on 2023/1/6
 **/
public class AdCustomManager {

    private static boolean adOpen = false;
    private static boolean sInit;
    private static AdCustomConfig config;
    //groMore是否初始化
    private static boolean groMoreInit = false;
    private static List<InitCallback> callbacks;
    private final static Handler mHandler = new Handler(Looper.getMainLooper());

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

    public static AdCustomConfig getConfig() {
        return config;
    }

    private static void doInit(@NonNull Context context) {
        if (!sInit) {
            sInit = true;
            initBeizi(context);
            initGroMore(context);
        }
    }


    public static void initBeizi(Context context) {
        BeiZis.init(context, config.getBzAppId());
    }

    public static void initGroMore(Context context) {
        if (!groMoreInit) {
            AdInitManager.isDebug = config.isDebug();
            AdInitManager.addInitCallback(new AdInitManager.InitCallback() {
                @Override
                public void onSuccess() {
                    groMoreInit = true;
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callbacks != null) {
                                for (InitCallback c : callbacks) {
                                    c.onSuccess();
                                }
                            }
                        }
                    });
                }

                @Override
                public void onFair() {
                    groMoreInit = false;
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callbacks != null) {
                                for (InitCallback c : callbacks) {
                                    c.onFair();
                                }
                            }
                        }
                    });
                }
            });
            AdInitManager.init(context, config.getCsjAppId(), config.getUserInfo());
        }
    }

    public interface InitCallback {
        void onSuccess();

        void onFair();
    }

    public static boolean isGroMoreInit() {
        return groMoreInit;
    }

}
