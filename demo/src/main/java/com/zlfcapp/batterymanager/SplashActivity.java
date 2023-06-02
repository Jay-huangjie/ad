package com.zlfcapp.batterymanager;

import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bytedance.sdk.openadsdk.mediation.MediationConstant;
import com.bytedance.sdk.openadsdk.mediation.ad.MediationSplashRequestInfo;
import com.mediation.ads.listener.AdListener;
import com.mediation.ads.provide.AdBannerProvide;
import com.mediation.ads.provide.AdInterstitialProvide;
import com.mediation.ads.provide.AdSplashProvide;

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
    }

}

