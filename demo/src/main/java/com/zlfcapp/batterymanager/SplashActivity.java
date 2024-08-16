package com.zlfcapp.batterymanager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bytedance.sdk.openadsdk.mediation.MediationConstant;
import com.bytedance.sdk.openadsdk.mediation.ad.MediationSplashRequestInfo;
import com.bytedance.tools.ui.ToolsActivity;
import com.mediation.ads.listener.AdListener;
import com.mediation.ads.provide.AdSplashProvide;
import com.zlfcapp.zlfcad.listener.SplashAdListener;


/**
 * created by hj on 2023/6/1.
 */
public class SplashActivity extends AppCompatActivity {

    private AdSplashProvide provide;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FrameLayout mSplashContainer = findViewById(R.id.adContainer);

        mSplashContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(SplashActivity.this, ToolsActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(SplashActivity.this,"测试工具不可用" + e, Toast.LENGTH_SHORT).show();
                }
            }
        });
        provide = new AdSplashProvide(this, mSplashContainer,
                new MediationSplashRequestInfo(MediationConstant.ADN_PANGLE, "888051093", "5100771", "") {
                }, new AdListener() {
            @Override
            public void onAdLoaded() {
//                if (provide != null) {
//                    provide.showAd();
//                }
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
            public void onAdFailed(int errorCode, String failedMsg) {

            }
        });
        provide.loadAd("102239173");



//        provide = new com.zlfcapp.zlfcad.core.AdSplashProvide(this, new SplashAdListener() {
//
//            @Override
//            public void onAdFailed(int i, String s) {
//
//            }
//
//            @Override
//            public void onAdLoaded() {
//                if (provide != null) {
//                    provide.showAd(mSplashContainer);
//                }
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
//            public void onAdTimeout() {
//
//            }
//        });



//        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
//        map.put(AdProviderType.CSJ.getType(), 1);
//        float splashWidthDp = UIUtils.getScreenWidthDp(this);
//        int splashWidthPx = UIUtils.getScreenWidthInPx(this);
//        int screenHeightPx = UIUtils.getScreenHeight(this);
//        float screenHeightDp = UIUtils.px2dip(this, screenHeightPx);
//        float splashHeightDp;
//        int splashHeightPx;
//        // 开屏高度 = 屏幕高度 - 下方预留的高度，demo中是预留了屏幕高度的1/5，因此开屏高度传入 屏幕高度*4/5
//        splashHeightDp = screenHeightDp * 7 / 8.f;
//        splashHeightPx = (int) (screenHeightPx * 7 / 8.f);
//        CsjProvider.Splash.INSTANCE.setImageAcceptedSize(splashWidthPx, screenHeightPx);
//        CsjProvider.Splash.INSTANCE.setExpressViewAcceptedSize(splashWidthDp, screenHeightDp);
//        AdHelperSplash.INSTANCE.show(SplashActivity.this, TogetherAdAlias.AD_SPLASH, map, mSplashContainer, new SplashListener() {
//            @Override
//            public void onAdFailed(@NonNull String providerType, @Nullable String failedMsg) {
//
//            }
//
//            @Override
//            public void onAdFailedAll(@Nullable String failedMsg) {
//                Log.e("HJ",failedMsg);
//            }
//
//            @Override
//            public void onAdStartRequest(@NonNull String providerType) {
//
//            }
//
//            @Override
//            public void onAdLoaded(@NonNull String providerType) {
//
//            }
//
//            @Override
//            public void onAdClicked(@NonNull String providerType) {
//
//            }
//
//            @Override
//            public void onAdExposure(@NonNull String providerType) {
//
//            }
//
//            @Override
//            public void onAdDismissed(@NonNull String providerType) {
//
//            }
//        });
    }

}

