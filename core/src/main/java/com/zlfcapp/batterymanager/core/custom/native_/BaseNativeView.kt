package com.zlfcapp.batterymanager.core.custom.native_

import android.view.ViewGroup
import com.zlfcapp.batterymanager.core.listener.NativeViewListener
import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

/*
 * Created by Matthew Chen on 2020-04-21.
 */
abstract class BaseNativeView {

    abstract fun showNative(@NotNull adProviderType: String, @NotNull adObject: Any, @NotNull container: ViewGroup, @Nullable listener: NativeViewListener? = null)

}