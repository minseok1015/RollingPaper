import java.io.FileInputStream
import java.util.Properties
import kotlin.script.experimental.api.ScriptCompilationConfiguration.Default.properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}


// local.properties 파일에서 값을 읽는 함수
fun getLocalProperty(key: String, project: Project): String {
    val properties = Properties()
    val localPropertiesFile = File(project.rootDir, "local.properties")
    if (localPropertiesFile.exists()) {
        FileInputStream(localPropertiesFile).use { stream ->
            properties.load(stream)
        }
    }
    return properties.getProperty(key) ?: ""
}

val kakaoNativeAppKey: String = getLocalProperty("kakao_native_app_key", project)
val kakaoOauthHost : String = getLocalProperty("kakao_oauth_host", project)

// 값을 로그로 출력하여 확인
println("Kakao Native App Key: $kakaoNativeAppKey")
println("Kakao OAuth Host: $kakaoOauthHost")

android {
    namespace = "com.example.rollingpaper"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.rollingpaper"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        // Kakao 네이티브 앱 키를 빌드 설정 필드로 추가
        buildConfigField("String", "KAKAO_NATIVE_APP_KEY", "\"$kakaoNativeAppKey\"")
        resValue ("string","kakao_oauth_host", kakaoOauthHost)
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.0")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0")
    implementation("androidx.navigation:navigation-compose:2.8.0-beta01")
    implementation("androidx.compose.ui:ui:1.7.0-beta01")
    implementation("androidx.compose.foundation:foundation:1.7.0-beta01")
//    implementation("androidx.compose.material3:material3-android:1.2.1")
//    implementation("com.android.support:design:28.0.0")
//    implementation("com.android.support:support-annotations:28.0.0")

    implementation("androidx.compose.foundation:foundation-layout:1.7.0-beta01")
    implementation("androidx.compose.material3:material3-android:1.2.1")
    implementation("com.google.chromeos:chromeos-lowlatencystylus:1.0.1")
    implementation("androidx.compose.runtime:runtime-livedata:1.6.7")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
//    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //카카오
    implementation("com.kakao.sdk:v2-all:2.20.1") // 전체 모듈 설치, 2.11.0 버전부터 지원
    implementation("com.kakao.sdk:v2-user:2.20.1") // 카카오 로그인 API 모듈
    implementation("com.kakao.sdk:v2-share:2.20.1") // 카카오톡 공유 API 모듈
    implementation("com.kakao.sdk:v2-talk:2.20.1") // 카카오톡 채널, 카카오톡 소셜, 카카오톡 메시지 API 모듈
    implementation("com.kakao.sdk:v2-friend:2.20.1") // 피커 API 모듈
    implementation("com.kakao.sdk:v2-navi:2.20.1") // 카카오내비 API 모듈
    implementation("com.kakao.sdk:v2-cert:2.20.1") // 카카오톡 인증 서비스 API 모듈

//    implementation("com.kakao.sdk:v2-link") // 메시지 카카오링크
}
