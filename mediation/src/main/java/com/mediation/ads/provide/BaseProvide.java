package com.mediation.ads.provide;

import android.app.Activity;
import android.util.Log;

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

    //加载广告
    public void loadAd(String id) {
        if (AdInitManager.initSuccess) {
            init(id);
        } else {
            mCallBack = new AdInitManager.InitCallback() {
                @Override
                public void onSuccess() {
                    init(id);
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

    public void log(String message){
        Log.e(TAG, message);
    }

    protected abstract void init(String id);

    protected abstract void onDestroy();


}
