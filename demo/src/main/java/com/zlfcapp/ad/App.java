package com.zlfcapp.ad;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.ifmvo.togetherad.core.TogetherAd;
import com.ifmvo.togetherad.csj.TogetherAdCsj;
import com.zlfcapp.batterymanager.AdProviderType;
import com.zlfcapp.batterymanager.TogetherAdAlias;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * created by hj on 2023/6/1.
 */
public class App extends Application {



    @Override
    public void onCreate() {
        super.onCreate();
//        AdInitManager.init(this,"5100771");
//        AdCustomManager.initAd(this,false, new AdCustomConfig.Builder()
//                .setBzAppId("21158")
//                .setBzSplashAdId("105288")
//                .setPublisherDid("11111")
//                .setCsjAppId("5100771")
//                .setGroMoreSplashAdId("102239173")
//                .setmAdNetworkSlotId("887371585")
//                .build());
        initAd();
    }


    public void initAd() {
        HashMap<String, String> csjmap = new HashMap<>();
        csjmap.put(TogetherAdAlias.AD_SPLASH, "887500862");
        TogetherAdCsj.INSTANCE.setInitCallback(new TTAdSdk.Callback() {
            @Override
            public void success() {
                Log.e("TogetherAd","初始化成功");
            }

            @Override
            public void fail(int i, String s) {

            }
        });
        TogetherAdCsj.INSTANCE.init(this, AdProviderType.CSJ.getType(), "5187100", "充电秀", csjmap);
        LinkedHashMap<String, Integer> adRadio = new LinkedHashMap<>();
        adRadio.put(AdProviderType.CSJ.getType(), 1);
        TogetherAd.INSTANCE.setPublicProviderRatio(adRadio);

    }
}
