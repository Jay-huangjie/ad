package com.ifmvo.togetherad.huawei.provider;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.huawei.appgallery.agd.agdpro.api.AdsContext;
import com.huawei.appgallery.agd.agdpro.api.IRewardVideoAd;
import com.huawei.appgallery.agd.agdpro.api.RewardInfo;
import com.huawei.appgallery.agd.agdpro.api.RewardLoadListener;
import com.huawei.appgallery.agd.core.api.AdSlot;
import com.ifmvo.togetherad.core.listener.RewardListener;
import com.ifmvo.togetherad.huawei.TogetherAdHw;

/**
 * create by hj on 2022/9/8
 **/
public abstract class HwProviderReward extends HwProviderInter {

    private IRewardVideoAd ad;

    @Override
    public void requestAndShowRewardAd(@NonNull FragmentActivity activity, @NonNull String adProviderType, @NonNull String alias, @NonNull RewardListener listener) {
        callbackRewardStartRequest(adProviderType, alias, listener);
        AdsContext adsContext = new AdsContext(activity);
        AdSlot adSlot = new AdSlot.Builder()
                .slotId(TogetherAdHw.INSTANCE.getIdMapKs().get(alias))                          // 广告槽位ID
                .darkMode(HuaweiProvider.ADSlot.INSTANCE.getDarkMode())    // 深色模式开关
                .mediaExtra(getJsonMediaExtra())                    // 个性化参数，透明传递，服务端请求参数
                .orientation(HuaweiProvider.Reward.INSTANCE.getOrientation())       // 广告方向
                .soundStatus(HuaweiProvider.Reward.INSTANCE.getSoundStatus())
                .ver(HuaweiProvider.Reward.INSTANCE.getVersion())
                .build();
        adsContext.loadRewardVideoAd(adSlot, new RewardLoadListener() {
            @Override
            public void onSuccess(IRewardVideoAd iRewardVideoAd) {
                ad = iRewardVideoAd;
                if (ad != null) {
                    ad.setInteractionListener(new IRewardVideoAd.InteractionListener() {
                        @Override
                        public void onRewardVerify(RewardInfo rewardInfo) {
                            if (rewardInfo != null) {
                                if (rewardInfo.isVerified()) {
                                    callbackRewardVerify(adProviderType, listener);
                                } else {
                                    callbackRewardFailed(adProviderType, alias, listener, rewardInfo.getCode(), rewardInfo.getMessage());
                                }
                            }
                        }

                        @Override
                        public void onAdExpired() {
                            //广告超期回调
                        }

                        @Override
                        public void onAdShow() {
                            callbackRewardShow(adProviderType, listener);
                        }

                        @Override
                        public void onAdClicked() {
                            callbackRewardClicked(adProviderType, listener);
                        }

                        @Override
                        public void onAdShowError(int i) {
                            callbackRewardFailed(adProviderType, alias, listener, i, getShowError(i));
                        }

                        @Override
                        public void onAdClose() {
                            callbackRewardClosed(adProviderType, listener);
                        }
                    });
                    callbackRewardLoaded(adProviderType, alias, listener);
                    ad.show(activity);
                }
            }

            @Override
            public void onError(int code, String message) {
                // 广告加载失败回调
                callbackRewardFailed(adProviderType, alias, listener, code, message);
            }
        });
    }

    @Override
    public void requestRewardAd(@NonNull FragmentActivity activity, @NonNull String adProviderType, @NonNull String alias, @NonNull RewardListener listener) {
        callbackRewardStartRequest(adProviderType, alias, listener);
        AdsContext adsContext = new AdsContext(activity);
        AdSlot adSlot = new AdSlot.Builder()
                .slotId(TogetherAdHw.INSTANCE.getIdMapKs().get(alias))                          // 广告槽位ID
                .darkMode(HuaweiProvider.ADSlot.INSTANCE.getDarkMode())    // 深色模式开关
                .mediaExtra(getJsonMediaExtra())                    // 个性化参数，透明传递，服务端请求参数
                .orientation(HuaweiProvider.Reward.INSTANCE.getOrientation())       // 广告方向
                .soundStatus(HuaweiProvider.Reward.INSTANCE.getSoundStatus())
                .ver(HuaweiProvider.Reward.INSTANCE.getVersion())
                .build();
        adsContext.loadRewardVideoAd(adSlot, new RewardLoadListener() {
            @Override
            public void onSuccess(IRewardVideoAd iRewardVideoAd) {
                ad = iRewardVideoAd;
                if (ad != null) {
                    ad.setInteractionListener(new IRewardVideoAd.InteractionListener() {
                        @Override
                        public void onRewardVerify(RewardInfo rewardInfo) {
                            if (rewardInfo != null) {
                                if (rewardInfo.isVerified()) {
                                    callbackRewardVerify(adProviderType, listener);
                                } else {
                                    callbackRewardFailed(adProviderType, alias, listener, rewardInfo.getCode(), rewardInfo.getMessage());
                                }
                            }
                        }

                        @Override
                        public void onAdExpired() {
                            //广告超期回调
                        }

                        @Override
                        public void onAdShow() {
                            callbackRewardShow(adProviderType, listener);
                        }

                        @Override
                        public void onAdClicked() {
                            callbackRewardClicked(adProviderType, listener);
                        }

                        @Override
                        public void onAdShowError(int i) {
                            callbackRewardFailed(adProviderType, alias, listener, i, getShowError(i));
                        }

                        @Override
                        public void onAdClose() {
                            callbackRewardClosed(adProviderType, listener);
                        }
                    });
                    callbackRewardLoaded(adProviderType, alias, listener);
                }
            }

            @Override
            public void onError(int code, String message) {
                // 广告加载失败回调
                callbackRewardFailed(adProviderType, alias, listener, code, message);
            }
        });
    }

    @Override
    public boolean showRewardAd(@NonNull FragmentActivity activity) {
        if (ad == null) {
            return false;
        }
        ad.show(activity);
        return true;
    }

}
