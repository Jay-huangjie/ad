# ad
### mediation 框架
1.初始化
```
AdInitManager.init(this,"csj id");
```

2.闪屏
```
       adSplashProvide = new AdSplashProvide(this, mSplashContainer,
               new MediationSplashRequestInfo(MediationConstant.ADN_PANGLE, "保底id", "csj appId", "") {
               }, new AdListener() {
           @Override
           public void onAdLoaded() {

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
               Log.e("HJ", "Error:" + errorCode + "---" + failedMsg);
           }
       });
       adSplashProvide.loadAd("id");
```

3.插屏
```
 AdInterstitialProvide provide = new AdInterstitialProvide(this, new AdListener() {
            @Override
            public void onAdLoaded() {

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
        provide.loadAd("id");
```

4.banner
```
AdBannerProvide provide = new AdBannerProvide(this, mSplashContainer, new AdListener() {
            @Override
            public void onAdLoaded() {

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
        provide.loadAd("id");
```

5.广告销毁
```
provide.destroy();
```
