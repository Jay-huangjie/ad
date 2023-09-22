package com.zlfcapp.batterymanager;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bytedance.sdk.openadsdk.mediation.MediationConstant;
import com.bytedance.sdk.openadsdk.mediation.ad.MediationSplashRequestInfo;
import com.ifmvo.togetherad.core.helper.AdHelperSplash;
import com.ifmvo.togetherad.core.listener.SplashListener;
import com.ifmvo.togetherad.core.utils.UIUtils;
import com.ifmvo.togetherad.csj.provider.CsjProvider;

import java.util.LinkedHashMap;


/**
 * created by hj on 2023/6/1.
 */
public class SplashActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FrameLayout mSplashContainer = findViewById(R.id.adContainer);
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        map.put(AdProviderType.CSJ.getType(), 1);
        float splashWidthDp = UIUtils.getScreenWidthDp(this);
        int splashWidthPx = UIUtils.getScreenWidthInPx(this);
        int screenHeightPx = UIUtils.getScreenHeight(this);
        float screenHeightDp = UIUtils.px2dip(this, screenHeightPx);
        float splashHeightDp;
        int splashHeightPx;
        // 开屏高度 = 屏幕高度 - 下方预留的高度，demo中是预留了屏幕高度的1/5，因此开屏高度传入 屏幕高度*4/5
        splashHeightDp = screenHeightDp * 7 / 8.f;
        splashHeightPx = (int) (screenHeightPx * 7 / 8.f);
        CsjProvider.Splash.INSTANCE.setImageAcceptedSize(splashWidthPx, splashHeightPx);
        CsjProvider.Splash.INSTANCE.setExpressViewAcceptedSize(splashWidthDp, splashHeightDp);
        AdHelperSplash.INSTANCE.show(this, TogetherAdAlias.AD_SPLASH, map, mSplashContainer, new SplashListener() {
            @Override
            public void onAdFailed(@NonNull String providerType, @Nullable String failedMsg) {

            }

            @Override
            public void onAdFailedAll(@Nullable String failedMsg) {
                  Log.e("HJ",failedMsg);
            }

            @Override
            public void onAdStartRequest(@NonNull String providerType) {

            }

            @Override
            public void onAdLoaded(@NonNull String providerType) {

            }

            @Override
            public void onAdClicked(@NonNull String providerType) {

            }

            @Override
            public void onAdExposure(@NonNull String providerType) {

            }

            @Override
            public void onAdDismissed(@NonNull String providerType) {

            }
        });
    }

}

