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


-keep class androidx.fragment.app*{
    *;
}
-keep class androidx.fragment.app.FragmentManager {
    *;
}

-keep class com.easyplexdemoapp.ui.streaming** {

 }

-keep class com.easyplexdemoapp.ui.search** {

 }

 -keep class com.easyplexdemoapp.ui.base.BaseActivity {

 }

-keep class com.easyplexdemoapp.ui.base** {

  }

-keep class com.easyplexdemoapp.ui.search** {

 }


-keep class com.easyplexdemoapp.ui.streaming** {

}

-keep class com.easyplexdemoapp.ui.downloadmanager.ui.settings** {

 }

-keep class com.easyplexdemoapp.ui.downloadmanager.ui.settings.sections** {

 }




# Keep generic signature of RxJava2 (R8 full mode strips signatures from non-kept items).
-keep,allowobfuscation,allowshrinking class io.reactivex.Flowable
-keep,allowobfuscation,allowshrinking class io.reactivex.Maybe
-keep,allowobfuscation,allowshrinking class io.reactivex.Observable
-keep,allowobfuscation,allowshrinking class io.reactivex.Single
-keep,allowobfuscation,allowshrinking class io.reactivex.rxjava3.core.Observable

# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Keep annotation default values (e.g., retrofit2.http.Field.encoded).
-keepattributes AnnotationDefault

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions
-dontwarn retrofit2.KotlinExtensions$*

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

# Keep inherited services.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface * extends <1>

# With R8 full mode generic signatures are stripped for classes that are not
# kept. Suspend functions are wrapped in continuations where the type argument
# is used.
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# R8 full mode strips generic signatures from return types if not kept.
-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>

-keepattributes Signature
-keep class kotlin.coroutines.Continuation

##### Google #####
# Databinding
-dontwarn android.databinding.**
# Dagger
-dontwarn com.google.errorprone.annotations.**
##################

##### OkHttp, Retrofit #####
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions
#################

##### Moshi for JSON #####
-dontwarn okio.**
-dontwarn javax.annotation.**
-keepclasseswithmembers class * {
    @com.squareup.moshi.* <methods>;
}
-keep @com.squareup.moshi.JsonQualifier interface *

-keepclassmembers class kotlin.Metadata {
    public <methods>;
}
###########################

##### gRPC #####
-dontwarn android.test.**
-dontwarn com.google.common.**
-dontwarn javax.naming.**
-dontwarn okio.**
-dontwarn org.junit.**
-dontwarn org.mockito.**
-dontwarn sun.reflect.**
# Ignores: can't find referenced class javax.lang.model.element.Modifier
-dontwarn com.google.errorprone.annotations.**
##################

##### Protobuf #####
-keep class * extends com.google.protobuf.** { *; }
-dontwarn com.google.protobuf.**
####################

##### Crashlytics #####
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception
-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**
#######################

##### Glide #####
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep class com.bumptech.glide.GeneratedAppGlideModuleImpl
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
#################

##### Wasabeef #####
# Protobuf
-keep class jp.wasabeef.data.proto.** { *; }
-keep class jp.wasabeef.data.grpc.** { *; }
##################


-keep public class com.easyplexdemoapp.ui.downloadmanager*
-keep public class com.easyplexdemoapp.di.*
-keep public class com.easyplexdemoapp.data.*
-keep public class com.easyplexdemoapp.ui.*
-keep public class com.easyplexdemoapp.ui.viewmodels.*
-keep public class io.reactivex.rxjava3.*

-keep class com.truenet.** {
      *;
}

-keepattributes Exceptions, InnerClasses, Signature, Deprecated, SourceFile,
LineNumberTable, *Annotation*, EnclosingMethod
-dontwarn android.webkit.JavascriptInterface

-dontwarn org.jetbrains.annotations.**

-keep class com.unity3d.** {
      *;
}

-keep class com.appodeal.** {
      *;
}

-keep class com.facebook.** {
      *;
}


-keep class com.ironsource.** {
      *;
}


-keep class com.truenet.** {
      *;
}


-keepclassmembers class com.ironsource.sdk.controller.IronSourceWebView$JSInterface {
    public *;
}
-keepclassmembers class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}
-keep public class com.google.android.gms.ads.** {
   public *;
}
-keep class com.ironsource.adapters.** { *;
}
-dontwarn com.ironsource.mediationsdk.**
-dontwarn com.ironsource.adapters.**
-keepattributes JavascriptInterface
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}


-keepattributes Exceptions, InnerClasses, Signature, Deprecated, SourceFile,
LineNumberTable, *Annotation*, EnclosingMethod
-dontwarn android.webkit.JavascriptInterface
-dontwarn com.startapp.**
-dontwarn org.jetbrains.annotations.**



# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
