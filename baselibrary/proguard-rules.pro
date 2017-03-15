# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Android\sdk/tools/proguard/proguard-android.txt
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

-optimizationpasses 5          # 指定代码的压缩级别
-dontusemixedcaseclassnames   # 是否使用大小写混合
-verbose                # 混淆时是否记录日志
-dontoptimize
-dontpreverify           # 混淆时是否做预校验
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法
-keepattributes *Annotation*
-keepattributes Annotation #保护注解
-keepattributes Signature  #避免混淆泛型
-keepattributes EnclosingMethod  #用到反射
-keepattributes Exceptions,InnerClasses #避免混淆类部类
-keepclasseswithmembernames class * {  # 指定不混淆所有的JNI方法
    native <methods>;
}

-keepclassmembers public class * extends android.view.View {
    void set*(%);
    void set*(%, %);
    void set*(%, %, %, %);
    void set*(%[]);
    void set*(**[]);
    void set*(!**Listener);

    % get*();
    %[] get*();
    **[] get*();
    !**Listener get*();
}

-keepclassmembers class * extends android.app.Activity {
    public void *(android.view.View);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepclassmembers class * implements android.os.Parcelable {
    public static final ** CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}

-keep public class com.google.vending.licensing.ILicensingService
-dontnote com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService
-dontnote com.android.vending.licensing.ILicensingService

-dontnote android.support.**
-dontwarn android.support.**
-keep public class * extends android.support.**
-keep class android.support.annotation.Keep
-keep @android.support.annotation.Keep class * {*;}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <methods>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <fields>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <init>(...);
}

-keep class com.jj.base.** {
    public protected <fields>;
    public protected <methods>;
}
#-keep class com.bumptech.glide.integration.okhttp3.OkHttpGlideModule
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule
-dontwarn okhttp3.**