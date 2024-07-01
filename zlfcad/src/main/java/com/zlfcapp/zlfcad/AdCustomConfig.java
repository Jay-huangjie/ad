package com.zlfcapp.zlfcad;

import com.bytedance.sdk.openadsdk.mediation.init.MediationConfigUserInfoForSegment;

/**
 * create by hj on 2023/1/6
 **/
public class AdCustomConfig {
    /**
     * Beizi配置参数信息
     */
    private String bzAppId;
    private String bzSplashAdId;
    /**
     * gromore配置参数
     **/
    // 流量分组的信息
    private MediationConfigUserInfoForSegment userInfo;
    private String publisherDid;
    private String csjAppId;
    private String groMoreSplashAdId;
    //穿山甲保底id
    private String mAdNetworkSlotId;
    //BeiZi 竞价
    private double mBindingPrice;

    private boolean debug;

    private String oaid;


    private AdCustomConfig(Builder builder) {
        bzAppId = builder.bzAppId;
        publisherDid = builder.publisherDid;
        csjAppId = builder.csjAppId;
        bzSplashAdId = builder.bzSplashAdId;
        groMoreSplashAdId = builder.groMoreSplashAdId;
        mAdNetworkSlotId = builder.mAdNetworkSlotId;
        debug = builder.debug;
        oaid = builder.oaid;
        if (builder.mBindingPrice == 0) {
            mBindingPrice = 1500;
        } else {
            mBindingPrice = builder.mBindingPrice;
        }
        if (builder.userInfo == null) {
            MediationConfigUserInfoForSegment userInfo = new MediationConfigUserInfoForSegment();
            userInfo.setUserId("msdk-demo");
            userInfo.setGender(MediationConfigUserInfoForSegment.GENDER_MALE);
            userInfo.setChannel("msdk-channel");
            userInfo.setSubChannel("msdk-sub-channel");
            userInfo.setAge(999);
            userInfo.setUserValueGroup("msdk-demo-user-value-group");
        } else {
            userInfo = builder.userInfo;
        }
    }

    public String getBzAppId() {
        return bzAppId;
    }

    public MediationConfigUserInfoForSegment getUserInfo() {
        return userInfo;
    }

    public String getPublisherDid() {
        return publisherDid;
    }

    public String getCsjAppId() {
        return csjAppId;
    }

    public String getBzSplashAdId() {
        return bzSplashAdId;
    }

    public String getGroMoreSplashAdId() {
        return groMoreSplashAdId;
    }

    public String getmAdNetworkSlotId() {
        return mAdNetworkSlotId;
    }

    public double getmBindingPrice() {
        return mBindingPrice;
    }

    public boolean isDebug() {
        return debug;
    }

    public String getOaid() {
        return oaid;
    }

    public static class Builder {
        private String oaid;
        private String bzAppId;
        private MediationConfigUserInfoForSegment userInfo;
        private String publisherDid;
        private String csjAppId;
        private String groMoreSplashAdId;
        private String mAdNetworkSlotId;
        private String bzSplashAdId;
        private double mBindingPrice;
        private boolean debug;

        public Builder() {
        }

        public AdCustomConfig build() {
            return new AdCustomConfig(this);
        }

        public Builder setBzAppId(String bzAppId) {
            this.bzAppId = bzAppId;
            return this;
        }

        public Builder setUserInfo(MediationConfigUserInfoForSegment userInfo) {
            this.userInfo = userInfo;
            return this;
        }

        public Builder setPublisherDid(String publisherDid) {
            this.publisherDid = publisherDid;
            return this;
        }

        public Builder setCsjAppId(String csjAppId) {
            this.csjAppId = csjAppId;
            return this;
        }

        public Builder setGroMoreSplashAdId(String groMoreSplashAdId) {
            this.groMoreSplashAdId = groMoreSplashAdId;
            return this;
        }

        public Builder setBzSplashAdId(String bzSplashAdId) {
            this.bzSplashAdId = bzSplashAdId;
            return this;
        }

        public Builder setmAdNetworkSlotId(String mAdNetworkSlotId) {
            this.mAdNetworkSlotId = mAdNetworkSlotId;
            return this;
        }

        public Builder setDebug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public Builder setOaid(String oaid) {
            this.oaid = oaid;
            return this;
        }

        public Builder setBindingPrice(double mBindingPrice) {
            this.mBindingPrice = mBindingPrice;
            return this;
        }
    }


}
