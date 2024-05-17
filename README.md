# ad
在根build.gradle里添加
```
repositories{
     maven { url "https://artifact.bytedance.com/repository/pangle"}
 }
```

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

### zlfcad 
清单文件里声明：
```
        <provider
            android:name="android.support.v4.content.FileProvider"
            android:authorities="${applicationId}.fileprovider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/beizi_file_path" />
        </provider>
```

xml:
```
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <external-path
        name="external"
        path="Beizi" />

    <external-path name="external_files" path="." />

</paths>
```

如果你已经声明了FileProvider和别的path，只要把xml里的paths复制进去即可
