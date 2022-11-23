package com.zlfcad.beizi;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.beizi.fusion.BeiZiCustomController;
import com.beizi.fusion.BeiZis;
import com.ifmvo.togetherad.core.TogetherAd;
import com.ifmvo.togetherad.core.entity.AdProviderEntity;
import com.zlfcad.beizi.provide.BeiziProvider;

import java.util.HashMap;
import java.util.Map;

/**
 * create by hj on 2022/11/22
 **/
public class TogetherAdBeizi {
    private static final TogetherAdBeizi ourInstance = new TogetherAdBeizi();

    public static TogetherAdBeizi getInstance() {
        return ourInstance;
    }

    private TogetherAdBeizi() {

    }

    private Map<String, String> idMaps = new HashMap<>();

    public Map<String, String> getIdMaps() {
        return idMaps;
    }

    public boolean isCanUseLocation = true;
    public boolean isCanUseWifiState = true;
    public boolean isCanUsePhoneState = true;
    public boolean isCanUseOaid = false;
    public boolean isCanUseGaid = false;

    public void init(@NonNull Context context,
                     @NonNull String adProviderType,
                     @NonNull String bzAppId,
                     Map<String, String> map) {
        init(context, adProviderType, bzAppId, map, null);
    }

    public void init(
            @NonNull Context context,
            @NonNull String adProviderType,
            @NonNull String bzAppId,
            Map<String, String> map,
            String providerClassPath) {
        if (TextUtils.isEmpty(providerClassPath)) {
            providerClassPath = BeiziProvider.class.getName();
        }
        if (map != null) {
            idMaps.putAll(map);
        }
        TogetherAd.INSTANCE.addProvider(new AdProviderEntity(adProviderType, providerClassPath, providerClassPath));
        BeiZis.init(context, bzAppId, new BeiZiCustomController() {
            /**
             * 是否允许SDK主动使用地理位置信息
             *
             * @return true可以获取，false禁止获取。默认为true
             */
            @Override
            public boolean isCanUseLocation() {
                return isCanUseLocation;
            }

            /**
             * 是否允许SDK主动使用ACCESS_WIFI_STATE权限
             *
             * @return true可以使用，false禁止使用。默认为true
             */
            @Override
            public boolean isCanUseWifiState() {
                return isCanUseWifiState;
            }

            /**
             * 是否允许SDK主动使用手机硬件参数，如：imei，imsi
             *
             * @return true可以使用，false禁止使用。默认为true
             */
            @Override
            public boolean isCanUsePhoneState() {
                return isCanUsePhoneState;
            }

            /**
             * 是否能使用Oaid
             *
             * @return true可以使用，false禁止使用。默认为true
             */
            @Override
            public boolean isCanUseOaid() {
                return isCanUseOaid;
            }

            /**
             * 是否能使用Gaid
             *
             * @return true可以使用，false禁止使用。默认为true
             */
            @Override
            public boolean isCanUseGaid() {
                return isCanUseGaid;
            }
        });
    }
}
