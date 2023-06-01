package com.zlfcapp.batterymanager;

import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zlfcapp.zlfcad.AdCustomConfig;
import com.zlfcapp.zlfcad.AdCustomManager;
import com.zlfcapp.zlfcad.core.AdSplashProvide;
import com.zlfcapp.zlfcad.listener.SplashAdListener;

import java.util.UUID;

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
        adSplashProvide = new AdSplashProvide(SplashActivity.this, new SplashAdListener() {
            @Override
            public void onAdLoaded() {
                Log.e("HJ", "加载成功");
                if (adSplashProvide != null) {
                    adSplashProvide.showAd(mSplashContainer);
                }
            }

            @Override
            public void onAdClicked() {
                Log.e("HJ", "onAdClicked");
            }

            @Override
            public void onAdExposure() {

            }

            @Override
            public void onAdDismissed() {

            }

            @Override
            public void onAdFailed(int errorCode, String failedMsg) {
                Log.e("HJ", "Error:" + errorCode + "---" + failedMsg);
            }
        });
    }
}

