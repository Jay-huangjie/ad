package com.mediation.ads.provide;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.Nullable;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.TTAdManager;
import com.bytedance.sdk.openadsdk.TTAdSdk;
import com.mediation.ads.AdInitManager;

/**
 * created by hj on 2023/6/2.
 */
public abstract class BaseProvide {

    public static final String TAG = "BaseProvide";

    private AdInitManager.InitCallback mCallBack;

    protected Activity mActivity;

    public BaseProvide(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void loadAd(String id) {
        loadAd(id, null);
    }


    //加载广告
    public void loadAd(String id,@Nullable AdSlot adSlot) {
        if (!AdInitManager.initSuccess) {
            AdInitManager.initSuccess = TTAdSdk.isInitSuccess();
        }
        if (AdInitManager.initSuccess) {
            init(id, adSlot);
        } else {
            mCallBack = new AdInitManager.InitCallback() {
                @Override
                public void onSuccess() {
                    init(id, adSlot);
                }

                @Override
                public void onFair() {
                    Log.e(TAG, "广告SDK初始化失败");
                }
            };
            AdInitManager.addInitCallback(mCallBack);
        }
    }

    //销毁广告
    public void destroy() {
        if (mCallBack != null) {
            AdInitManager.removeInitCallback(mCallBack);
        }
        mActivity = null;
        onDestroy();
    }

    public void log(String message) {
        Log.e(TAG, message);
    }

    protected abstract void init(String id, AdSlot adSlot);

    protected abstract void onDestroy();


}
