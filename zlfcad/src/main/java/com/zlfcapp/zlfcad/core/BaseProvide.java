package com.zlfcapp.zlfcad.core;

import android.app.Activity;
import android.view.ViewGroup;

/**
 * created by hj on 2023/9/21.
 */
public abstract class BaseProvide {

    protected Activity mActivity;

    private boolean adShow;

    public BaseProvide(Activity mActivity) {
        this.mActivity = mActivity;
    }

    protected void setAdShow(boolean isShow) {
        adShow = isShow;
    }

    public boolean isAdShow() {
        return adShow;
    }

    public abstract void loadAd();

    public abstract void showAd(ViewGroup contain);

    public abstract void destroy();
}
