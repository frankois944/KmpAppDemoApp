# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/wojta/adt-bundle/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.kts.
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

-keepattributes *Annotation*
-dontnote kotlinx.serialization.SerializationKt
-keep,includedescriptorclasses class com.cacd2.cacdgame.android.**$$serializer { *; }
-keepclassmembers class com.cacd2.cacdgame.android.** {
    *** Companion;
}
-keepclasseswithmembers class com.cacd2.cacdgame.android.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.ConscryptHostnameVerifier
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE
-dontwarn org.slf4j.impl.StaticLoggerBinder

# Firebase
-keep class com.firebase.** { *; }
-keep class org.apache.** { *; }
-keepnames class com.fasterxml.jackson.** { *; }
-keepnames class javax.servlet.** { *; }
-keepnames class org.ietf.jgss.** { *; }
-dontwarn org.apache.**
-dontwarn org.w3c.dom.**
-keep class com.google.android.gms.** { *; }
-keep class com.cacd2.cacdgame.datasource.api.base.** { *; }
-keep class com.cacd2.cacdgame.datasource.api.new.** { *; }

-keepattributes AutoValue
-keep class com.google.firebase.installations.** {
  *;
}
-keep interface com.google.firebase.installations.** {
  *;
}
