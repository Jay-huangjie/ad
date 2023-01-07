# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

 #oaid 不同的版本混淆代码不太一致，你注意你接入的oaid版本
 -dontwarn com.bun.**
 -keep class com.bun.** {*;}
 -keep class a.**{*;}
 -keep class XI.CA.XI.**{*;}
 -keep class XI.K0.XI.**{*;}
 -keep class XI.XI.K0.**{*;}
 -keep class XI.vs.K0.**{*;}
 -keep class XI.xo.XI.XI.**{*;}
 -keep class com.asus.msa.SupplementaryDID.**{*;}
 -keep class com.asus.msa.sdid.**{*;}
 -keep class com.huawei.hms.ads.identifier.**{*;}
 -keep class com.samsung.android.deviceidservice.**{*;}
 -keep class com.zui.opendeviceidlibrary.**{*;}
 -keep class org.json.**{*;}
 -keep public class com.netease.nis.sdkwrapper.Utils {public <methods>;}

 #gdt
 -keep class com.qq.e.** {
     public protected *;
 }
 -keep class android.support.v4.**{
     public *;
 }
 -keep class android.support.v7.**{
     public *;
 }
 -keep class yaq.gdtadv{
     *;
 }
 -keep class com.qq.e.** {
     *;
 }
 -keep class com.tencent.** {
     *;
 }
 -keep class cn.mmachina.JniClient {
     *;
 }
 -keep class c.t.m.li.tsa.** {
     *;
 }

 -keep class * extends java.lang.annotation.Annotation { *; }
 -keep interface * extends java.lang.annotation.Annotation { *; }

 -keep, allowobfuscation class com.qq.e.comm.plugin.services.SDKServerService {*;}
 -keepclassmembers, allowobfuscation class com.qq.e.comm.plugin.net.SecurePackager {
     public *;
 }

 -keepclasseswithmembers,includedescriptorclasses class * {
 native <methods>;
 }

 -keep class * extends com.qq.e.mediation.interfaces.BaseNativeUnifiedAd { *; }
 -keep class * extends com.qq.e.mediation.interfaces.BaseSplashAd { *; }
 -keep class * extends com.qq.e.mediation.interfaces.BaseRewardAd { *; }


 -keep class com.zlfcapp.ad.gdt.** { *; }

 -keep class com.qq.e.** {
     public protected *;
 }
 -keep class android.support.v4.**{
     public *;
 }
 -keep class android.support.v7.**{
     public *;
 }

 #csj
 #------------------------穿山甲的混淆---------------------------#
 -keep class com.bytedance.sdk.openadsdk.** { *; }
 -keep class com.bytedance.frameworks.** { *; }

 -keep class ms.bd.c.Pgl.**{*;}
 -keep class com.bytedance.mobsec.metasec.ml.**{*;}

 -keep class com.ss.android.**{*;}

 -keep class com.bytedance.embedapplog.** {*;}
 -keep class com.bytedance.embed_dr.** {*;}

 -keep class com.bykv.vk.** {*;}

 -keep class com.zlfcapp.ad.csj.** { *; }
 #----------------------新增------------------------------#
 -keepclassmembers class * {
     *** getContext(...);
     *** getActivity(...);
     *** getResources(...);
     *** startActivity(...);
     *** startActivityForResult(...);
     *** registerReceiver(...);
     *** unregisterReceiver(...);
     *** query(...);
     *** getType(...);
     *** insert(...);
     *** delete(...);
     *** update(...);
     *** call(...);
     *** setResult(...);
     *** startService(...);
     *** stopService(...);
     *** bindService(...);
     *** unbindService(...);
     *** requestPermissions(...);
     *** getIdentifier(...);
    }

 -keep class com.bytedance.pangle.** {*;}
 -keep class com.bytedance.sdk.openadsdk.** { *; }

 -keep class ms.bd.c.Pgl.**{*;}
 -keep class com.bytedance.mobsec.metasec.ml.**{*;}

 -keep class com.bytedance.embedapplog.** {*;}
 -keep class com.bytedance.embed_dr.** {*;}

 -keep class com.bykv.vk.** {*;}

 -keep class com.lynx.** { *; }

 -keep class com.ss.android.**{*;}

 -keep class android.support.v4.app.FragmentActivity{}
 -keep class androidx.fragment.app.FragmentActivity{}
 -keepclassmembernames class *{
 	*** _GET_PLUGIN_PKG();
 }
 -keep class androidx.fragment.app.FragmentFactory{
     *** sClassMap;
 }
 -keep class com.volcengine.zeus.LocalBroadcastManager{
     *** getInstance(**);
     *** registerReceiver(**,**);
     *** unregisterReceiver(**);
     *** sendBroadcast(**);
     *** sendBroadcastSync(**);
 }
 -keep class com.bytedance.pangle.LocalBroadcastManager{
     *** getInstance(**);
     *** registerReceiver(**,**);
     *** unregisterReceiver(**);
     *** sendBroadcast(**);
     *** sendBroadcastSync(**);
 }
 -keep class com.volcengine.StubConfig{
     *;
 }


 -keep class android.support.v4.app.FragmentActivity{}
 -keep class androidx.fragment.app.FragmentActivity{}
 -keepclassmembernames class *{
 	*** _GET_PLUGIN_PKG();
 }
 -keep class androidx.fragment.app.FragmentFactory{
     *** sClassMap;
 }
 -keep class com.volcengine.zeus.LocalBroadcastManager{
     *** getInstance(**);
     *** registerReceiver(**,**);
     *** unregisterReceiver(**);
     *** sendBroadcast(**);
     *** sendBroadcastSync(**);
 }
 -keep class com.bytedance.pangle.LocalBroadcastManager{
     *** getInstance(**);
     *** registerReceiver(**,**);
     *** unregisterReceiver(**);
     *** sendBroadcast(**);
     *** sendBroadcastSync(**);
 }
 -keep class com.volcengine.StubConfig{
     *;
 }

 # GroMore混淆
 -keep class bykvm*.**
 -keep class com.bytedance.msdk.adapter.**{ public *; }
 -keep class com.bytedance.msdk.api.** {
  public *;
 }
 -keep class com.bytedance.msdk.base.TTBaseAd{*;}
 -keep class com.bytedance.msdk.adapter.TTAbsAdLoaderAdapter{
     public *;
     protected <fields>;
 }

 # 倍孜混淆
 -dontwarn com.beizi.fusion.**
 -dontwarn com.beizi.ad.**
 -keep class com.beizi.fusion.** {*; }
 -keep class com.beizi.ad.** {*; }

 -keep class com.qq.e.** {
     public protected *;
 }

 -keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

 -dontwarn  org.apache.**