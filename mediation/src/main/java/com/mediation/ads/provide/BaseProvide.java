package com.mediation.ads.provide;

import android.util.Log;

import com.mediation.ads.AdInitManager;

/**
 * created by hj on 2023/6/2.
 */
public abstract class BaseProvide {

    private AdInitManager.InitCallback mCallBack;

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
                    Log.e("AD", "广告SDK初始化失败");
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
        onDestroy();
    }


    protected abstract void init(String id);

    protected abstract void onDestroy();


}
