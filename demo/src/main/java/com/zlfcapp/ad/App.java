package com.zlfcapp.ad;

import android.app.Application;

import com.zlfcapp.zlfcad.AdCustomConfig;
import com.zlfcapp.zlfcad.AdCustomManager;

/**
 * created by hj on 2023/6/1.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AdCustomManager.initAd(this,false, new AdCustomConfig.Builder()
                .setBzAppId("21158")
                .setBzSplashAdId("105288")
                .setPublisherDid("11111")
                .setCsjAppId("5100771")
                .setGroMoreSplashAdId("102239173")
                .setmAdNetworkSlotId("887371585")
                .build());
//        AdCustomManager.initAd(this, true, new AdCustomConfig.Builder()
//                .setBzAppId("21158")
//                .setBzSplashAdId("105288")
//                .setPublisherDid("8888465456456")
//                .setCsjAppId("5100771")
//                .setGroMoreSplashAdId("102239173")
//                .setmAdNetworkSlotId("887371585")
//                .build());
    }
}
