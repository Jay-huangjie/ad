package com.zlfcapp.ad.core.custom.native_


/*
 * Created by Matthew Chen on 2020-04-21.
 */
abstract class BaseNativeTemplate {

    abstract fun getNativeView(adProviderType: String): BaseNativeView?

}