plugins {
    id("base-module")
    id("com.android.library")
    kotlin("multiplatform")
    id("dev.icerock.mobile.multiplatform.android-manifest")
    id("dev.icerock.mobile.multiplatform.android-sources")
}

kotlin {
    android()
    jvm()
}

android {
    compileSdk = com.gnoemes.shimori.AndroidConfig.compileSdk

    defaultConfig {
        minSdk = com.gnoemes.shimori.AndroidConfig.minSdk
        targetSdk = com.gnoemes.shimori.AndroidConfig.targetSdk
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}