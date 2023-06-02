package com.zlfcapp.ad;

import android.app.Application;

import com.mediation.ads.AdInitManager;

/**
 * created by hj on 2023/6/1.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        AdInitManager.init(this,"5158498");

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
