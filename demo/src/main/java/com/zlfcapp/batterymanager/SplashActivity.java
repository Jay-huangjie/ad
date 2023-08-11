package com.zlfcapp.batterymanager;

import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zlfcapp.zlfcad.AdCustomManager;
import com.zlfcapp.zlfcad.core.AdSplashProvide;
import com.zlfcapp.zlfcad.listener.SplashAdListener;


/**
 * created by hj on 2023/6/1.
 */
public class SplashActivity extends AppCompatActivity {

    private AdSplashProvide adSplashProvide;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FrameLayout mSplashContainer = findViewById(R.id.adContainer);
        adSplashProvide = new AdSplashProvide(this, new SplashAdListener() {
            @Override
            public void onAdLoaded() {
                if (adSplashProvide != null) {
                    adSplashProvide.showAd(mSplashContainer);
                }
            }

            @Override
            public void onAdClicked() {

            }

            @Override
            public void onAdExposure() {

            }

            @Override
            public void onAdDismissed() {

            }

            @Override
            public void onAdTimeout() {

            }

            @Override
            public void onAdFailed(int errorCode, String failedMsg) {

            }
        });

//        adSplashProvide = new AdSplashProvide(this, mSplashContainer,
//                new MediationSplashRequestInfo(MediationConstant.ADN_PANGLE, "887371585", "5100771", "") {
//                }, new AdListener() {
//            @Override
//            public void onAdLoaded() {
//
//            }
//
//            @Override
//            public void onAdClicked() {
//
//            }
//
//            @Override
//            public void onAdExposure() {
//
//            }
//
//            @Override
//            public void onAdDismissed() {
//
//            }
//
//            @Override
//            public void onAdFailed(int errorCode, String failedMsg) {
//                Log.e("HJ", "Error:" + errorCode + "---" + failedMsg);
//            }
//        });
//        adSplashProvide.loadAd("102239173");
    }

}

