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
#noinspection ShrinkerUnresolvedReference
-assumenosideeffects class rikka.shizuku.server.util.Logger {
    public *** d(...);
}

-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile


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

 ## pangle 穿山甲原有的
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

 -keep class com.bytedance.sdk.openadsdk.** { *; }
 -keep class com.bytedance.frameworks.** { *; }

 -keep class ms.bd.c.Pgl.**{*;}
 -keep class com.bytedance.mobsec.metasec.ml.**{*;}

 -keep class com.ss.android.**{*;}

 -keep class com.bytedance.embedapplog.** {*;}
 -keep class com.bytedance.embed_dr.** {*;}

 -keep class com.bykv.vk.** {*;}


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

 # baidu sdk 不接入baidu sdk可以不引入
 -ignorewarnings
 -dontwarn com.baidu.mobads.sdk.api.**
 -keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
 }

 -keepclassmembers enum * {
     public static **[] values();
     public static ** valueOf(java.lang.String);
 }

 -keep class com.baidu.mobads.** { *; }
 -keep class com.style.widget.** {*;}
 -keep class com.component.** {*;}
 -keep class com.baidu.ad.magic.flute.** {*;}
 -keep class com.baidu.mobstat.forbes.** {*;}


 # Admob 不接入admob sdk可以不引入
 -keep class com.google.android.gms.ads.MobileAds {
  public *;
 }

 #sigmob  不接入sigmob sdk可以不引入
 -dontwarn android.support.v4.**
 -keep class android.support.v4.** { *; }
 -keep interface android.support.v4.** { *; }
 -keep public class * extends android.support.v4.**

 -keep class sun.misc.Unsafe { *; }
 -dontwarn com.sigmob.**
 -keep class com.sigmob.**.**{*;}
 -keep interface com.sigmob.**.**{*;}
 -keep class com.czhj.**{*;}
 -keep interface com.czhj.**{*;}

 #oaid 不同的版本混淆代码不太一致，你注意你接入的oaid版本 ，不接入oaid可以不添加
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


 #klevin 游可赢
 -keep class com.tencent.tgpa.lite.**{*;}
 -keep class com.ihoc.mgpa.deviceid.**{*;}
 -keep class com.tencent.klevin.**{*;}


 #Mintegral 不接入Mintegral sdk，可以不引入
 -keepattributes Signature
 -keepattributes *Annotation*
 -keep class com.mbridge.** {*; }
 -keep interface com.mbridge.** {*; }
 -keep class android.support.v4.** { *; }
 -dontwarn com.mbridge.**
 -keep class **.R$* { public static final int mbridge*; }

 # Add project specific ProGuard rules here.
 # By default, the flags in this file are appended to flags specified
 # in /Users/sumirrowu/Library/Android/sdk/tools/proguard/proguard-android.txt
 # You can edit the include path and order by changing the proguardFiles
 # directive in build.gradle.
 #
 # For more details, see
 #   http://developer.android.com/guide/developing/tools/proguard.html

 # Add any project specific keep options here:

 # If your project uses WebView with JS, uncomment the following
 # and specify the fully qualified class name to the JavaScript interface
 # class:
 #-keepclassmembers class fqcn.of.javascript.interface.for.webview {
 #   public *;
 #}

 -optimizationpasses 5

 #混淆时不会产生形形色色的类名
 -dontusemixedcaseclassnames

 #指定不去忽略非公共的库类
 -dontskipnonpubliclibraryclasses

 #不预校验
 #-dontpreverify

 #不优化输入的类文件
 -dontoptimize

 -ignorewarnings

 -verbose

 #优化
 -optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

 #保护内部类
 -keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod
 -keep class com.bytedance.sdk.openadsdk.** {*;}
 -keep public interface com.bytedance.sdk.openadsdk.downloadnew.** {*;}
 -keep class com.pgl.sys.ces.* {*;}