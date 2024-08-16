package com.zlfcapp.ad;

import android.app.Application;

import com.mediation.ads.AdInitManager;
import com.zlfcapp.zlfcad.AdCustomConfig;
import com.zlfcapp.zlfcad.AdCustomManager;

/**
 * created by hj on 2023/6/1.
 */
public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        AdInitManager.init(this,"5100771");
//        initZlfcad();
//        initAd();
    }

    public void initZlfcad() {
        AdCustomManager.initAd(this, true, new AdCustomConfig.Builder()
                .setBzAppId("21158")
                .setDebug(true)
                .setBzSplashAdId("105288")
                .setPublisherDid("11111")
                .setCsjAppId("5100771")
                .setGroMoreSplashAdId("102239173")
                .setmAdNetworkSlotId("887371585")
                .build());
    }


//    public void initAd() {
//        HashMap<String, String> csjmap = new HashMap<>();
//        csjmap.put(TogetherAdAlias.AD_SPLASH, "887500862");
//        TogetherAdCsj.INSTANCE.setInitCallback(new TTAdSdk.Callback() {
//            @Override
//            public void success() {
//                Log.e("TogetherAd","初始化成功");
//            }
//
//            @Override
//            public void fail(int i, String s) {
//
//            }
//        });
//        TogetherAdCsj.INSTANCE.init(this, AdProviderType.CSJ.getType(), "5187100", "充电秀", csjmap);
//        LinkedHashMap<String, Integer> adRadio = new LinkedHashMap<>();
//        adRadio.put(AdProviderType.CSJ.getType(), 1);
//        TogetherAd.INSTANCE.setPublicProviderRatio(adRadio);
//
//    }
}
