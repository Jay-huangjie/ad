package com.zlfcapp.zlfcad;

import com.bytedance.msdk.api.UserInfoForSegment;
import com.bytedance.msdk.api.v2.GMConfigUserInfoForSegment;

import java.util.Random;

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
    private GMConfigUserInfoForSegment userInfo;
    private String publisherDid;
    private String groMoreAppId;
    private String groMoreSplashAdId;
    //穿山甲保底id
    private String mAdNetworkSlotId;

    private AdCustomConfig(Builder builder) {
        bzAppId = builder.bzAppId;
        publisherDid = builder.publisherDid;
        groMoreAppId = builder.groMoreAppId;
        bzSplashAdId = builder.bzSplashAdId;
        groMoreSplashAdId = builder.groMoreSplashAdId;
        mAdNetworkSlotId = builder.mAdNetworkSlotId;
        if (builder.userInfo == null) {
            userInfo = new GMConfigUserInfoForSegment();
            userInfo.setUserId("user" + new Random().nextInt());
            userInfo.setGender(UserInfoForSegment.GENDER_MALE);
            userInfo.setChannel("channel");
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

    public GMConfigUserInfoForSegment getUserInfo() {
        return userInfo;
    }

    public String getPublisherDid() {
        return publisherDid;
    }

    public String getGroMoreAppId() {
        return groMoreAppId;
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

    public static class Builder {
        private String bzAppId;
        private GMConfigUserInfoForSegment userInfo;
        private String publisherDid;
        private String groMoreAppId;
        private String groMoreSplashAdId;
        private String mAdNetworkSlotId;
        private String bzSplashAdId;

        public Builder() {
        }

        public AdCustomConfig build() {
            return new AdCustomConfig(this);
        }

        public Builder setBzAppId(String bzAppId) {
            this.bzAppId = bzAppId;
            return this;
        }

        public Builder setUserInfo(GMConfigUserInfoForSegment userInfo) {
            this.userInfo = userInfo;
            return this;
        }

        public Builder setPublisherDid(String publisherDid) {
            this.publisherDid = publisherDid;
            return this;
        }

        public Builder setGroMoreAppId(String groMoreAppId) {
            this.groMoreAppId = groMoreAppId;
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
    }


}
