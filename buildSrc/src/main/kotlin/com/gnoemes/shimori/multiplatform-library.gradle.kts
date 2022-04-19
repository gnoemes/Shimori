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
    compileSdk = com.gnoemes.shimori.Application.compileSdk

    defaultConfig {
        minSdk = com.gnoemes.shimori.Application.minSdk
        targetSdk = com.gnoemes.shimori.Application.targetSdk
    }
}