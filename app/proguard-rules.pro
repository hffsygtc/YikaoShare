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

-keep public class com.google.android.material.bottomnavigation.BottomNavigationView { *; }
-keep public class com.google.android.material.bottomnavigation.BottomNavigationMenuView { *; }
-keep public class com.google.android.material.bottomnavigation.BottomNavigationPresenter { *; }
-keep public class com.google.android.material.bottomnavigation.BottomNavigationItemView { *; }


#七牛播放器
-keep class com.qiniu.qmedia.** {*;}
-keep class com.qiniu.qplayer2ext.** {*;}
#七牛云对象存储
-keep class com.qiniu.**{*;}
-keep class com.qiniu.**{public <init>();}
-ignorewarnings



# Basic ProGuard rules
-dontwarn
-keepattributes *Annotation*, Signature
-keepnames class com.google.gson.** { *; }
-keep public class ** implements java.io.Serializable
-keep public class * extends android.view.View
-keep public class * extends androidx.fragment.app.Fragment
-keep public class * extends androidx.appcompat.app.AppCompatActivity
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference

# androidx.core:core-ktx, androidx.appcompat:appcompat, androidx.constraintlayout:constraintlayout
# These libraries usually handle ProGuard rules internally, no additional rules are needed.

# junit:junit, androidx.test.ext:junit, androidx.test.espresso:espresso-core
# These are test libraries and should not be included in the release build.

# JetpackMvvm
-keep class com.hegaojian.jetpackmvvm.** { *; }

# UltimateBarX
-keep class com.zackratos.ultimatebarx.** { *; }

# MMKV
-keep class com.tencent.mmkv.** { *; }
-keepclassmembers class com.tencent.mmkv.** {
    native <methods>;
}

# LoadSir
-keep class com.kingja.loadsir.** { *; }

# Lottie
-keep class com.airbnb.lottie.** { *; }
-keepattributes Signature
-keepclassmembers class * {
    @com.airbnb.lottie.** <fields>;
}
-keepclassmembers class * {
    @com.airbnb.lottie.** <methods>;
}

# Yanzhenjie RecyclerView
-keep class com.yanzhenjie.recyclerview.x.** { *; }

# BaseRecyclerViewAdapterHelper
-keep class com.chad.library.adapter.base.** { *; }
-keep interface com.chad.library.adapter.base.** { *; }

# AndroidAutoSize
-keep class me.jessyan.autosize.** { *; }

# BannerViewPager & ViewPagerIndicator
-keep class com.zhpan.bannerview.** { *; }
-keep class com.zhpan.indicator.** { *; }

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public class * extends com.bumptech.glide.manager.RequestManagerRetriever
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# BottomNavigationViewEx
-keep class com.ittianyu.bottomnavigationviewex.** { *; }

# ExpandableLayout
-keep class com.skydoves.expandablelayout.** { *; }

# Navigation
# These libraries usually handle ProGuard rules internally, no additional rules are needed.

# zxing-lite
-keep class com.jenly.zxinglib.** { *; }

# PermissionX
-keep class com.guolin.permissionx.** { *; }

# WebViewLib
-keep class cn.yc.library.** {*;}

# TbsSdk
-keep class com.tencent.smtt.sdk.** { *; }

# YCSlideLib
-keep class cn.yc.slide.** { *; }

# MaterialProgressBar
-keep class me.zhanghai.android.materialprogressbar.** { *; }

# CameraX
# These libraries usually handle ProGuard rules internally, no additional rules are needed.

# Kotlin, Coroutines
# These libraries usually handle ProGuard rules internally, no additional rules are needed.

# CityPicker
-keep class com.crazyandcoder.citypicker.** { *; }

# PictureSelector
-keep class io.github.lucksiege.pictureselector.** { *; }

# Android-PickerView
-keep class com.contrarywind.view.** { *; }

# Qiniu Android SDK
-keep class com.qiniu.** { *; }

# WeChat SDK
-keep class com.tencent.mm.opensdk.** {*;}

# Alipay SDK
-keep class com.alipay.sdk.** {*;}

# ExoPlayer
-keep class com.google.android.exoplayer2.** { *; }
-keep class com.google.android.exoplayer2.source.ConcatenatingMediaSource { *; }
-keep class com.google.android.exoplayer2.source.MediaSourceFactory { *; }
-keep class com.google.android.exoplayer2.source.dash.DefaultDashChunkSource$Factory { *; }
-keep class com.google.android.exoplayer2.source.hls.DefaultHlsExtractorFactory { *; }
-keep class com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource$Factory { *; }
-keep class com.google.android.exoplayer2.upstream.LoaderErrorThrower { *; }
#-keep class com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory { *; }
-keep class com.google.android.exoplayer2.upstream.cache.SimpleCache { *; }
#-keep class com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory { *; }
-keep class com.google.android.exoplayer2.upstream.DefaultDataSourceFactory { *; }

# Add this global rule if you are using R8
#-keepattributes EnclosingMethod