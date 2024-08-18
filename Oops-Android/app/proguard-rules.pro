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
-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
-renamesourcefileattribute SourceFile

# --- 추가 ---
# Keep all classes in the package
# remote
-keep class com.oops.oops_android.data.remote.** { *; }

# Home
-keep class com.oops.oops_android.ui.Main.Home.CalendarIWeeklytem { *; }
-keep class com.oops.oops_android.ui.Main.Home.HomeInventoryItem { *; }
-keep class com.oops.oops_android.ui.Main.Home.MonthlyItem { *; }
-keep class com.oops.oops_android.ui.Main.Home.StuffItem { *; }
-keep class com.oops.oops_android.ui.Main.Home.TodoItem { *; }
-keep class com.oops.oops_android.ui.Main.Home.TodoListItem { *; }
-keep class com.oops.oops_android.ui.Main.Home.TodoListItem { *; }
-keep class com.oops.oops_android.ui.Main.Home.TodoCheckModifyItem { *; }
-keep class com.oops.oops_android.ui.Main.Home.TodoModifyItem { *; }
-keep class com.oops.oops_android.ui.Main.Home.TodoCreateItem { *; }

# inventory
-keep class com.oops.oops_android.ui.Main.Inventory.CategoryItemUI { *; }
-keep class com.oops.oops_android.ui.Main.Inventory.CategoryList { *; }
-keep class com.oops.oops_android.ui.Main.Inventory.Stuff.StuffAddItem { *; }
-keep class com.oops.oops_android.ui.Main.Inventory.Stuff.StuffItemUI { *; }

# mypage
-keep class com.oops.oops_android.ui.Main.MyPage.MyPageItem { *; }
-keep class com.oops.oops_android.ui.Main.MyPage.NoticeItem { *; }
-keep class com.oops.oops_android.ui.Main.MyPage.WithdrawalItem { *; }

# sting
-keep class com.oops.oops_android.ui.Main.Sting.FriendsItem { *; }
-keep class com.oops.oops_android.ui.Main.Sting.StingRequestModel { *; }
-keep class com.oops.oops_android.ui.Main.Sting.StingAcceptModel { *; }
-keep class com.oops.oops_android.ui.Main.Sting.StingRefuseModel { *; }

# Keep Gson specific annotations
-keep class com.google.gson.** { *; }
-keepattributes *Annotation*

# Keep classes with Gson annotations
-keep class com.yourpackage.** {
    @com.google.gson.annotations.SerializedName <fields>;
}

# Keep default constructor for all model classes
-keepclassmembers class com.oops.oops_android.data.remote.** {
    public <init>();
}

# Strip out logging calls
-assumenosideeffects class android.util.Log {
    public static int v(...);
    public static int i(...);
    public static int d(...);
    public static int w(...);
    public static int e(...);
}

####-----------------------------------------------------------------------

# Please add these rules to your existing keep rules in order to suppress warnings.
# This is generated automatically by the Android Gradle plugin.
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

# Keep generic signature of Call, Response (R8 full mode strips signatures from non-kept items).
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response

# With R8 full mode generic signatures are stripped for classes that are not
# kept. Suspend functions are wrapped in continuations where the type argument
# is used.
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation