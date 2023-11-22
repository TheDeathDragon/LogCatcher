import com.android.build.gradle.internal.api.ApkVariantOutputImpl

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.sunritel.logcatcher"
    compileSdk = 34

    signingConfigs {
        getByName("debug") {
            keyAlias = "android"
            keyPassword = "android"
            storeFile = file("T.jks")
            storePassword = "android"
        }
        create("release") {
            keyAlias = "android"
            keyPassword = "android"
            storeFile = file("T.jks")
            storePassword = "android"
        }
    }

    defaultConfig {
        applicationId = "com.sunritel.logcatcher"
        minSdk = 28
        targetSdk = 34
        versionCode = 3
        versionName = "3.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
            isDebuggable = false
        }
        debug {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    applicationVariants.all {
        outputs.all {
            if (this is ApkVariantOutputImpl) {
                outputFileName = "LogCatcher.apk"
            }
        }
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.preference:preference-ktx:1.2.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
}