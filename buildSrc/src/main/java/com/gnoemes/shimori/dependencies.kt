package com.gnoemes.shimori

object Application {
    const val id = "com.gnoemes.shimori"
}

object Modules {
    //base
    const val app = ":app"
    const val base = ":base"
    const val baseAndroid = ":base-android"
    const val common = ":common"
    const val navigation = ":navigation"

    //data
    const val dataBase = ":data-base"
    const val data = ":data"
    const val shikimori = ":shikimori"
    const val shikimoriAuth = ":shikimori-auth"

    //domain
    const val domain = ":domain"
    const val model = ":model"

    //features
    const val featureSearch = ":features:search"
    const val featureCalendar = ":features:calendar"
    const val featureRates = ":features:rates"
}

object Versions {
    const val compileSdk = 29
    const val minSdk = 16
    const val targetSdk = 29

    const val material = "1.2.0-alpha05"
}

object Libs {
    const val androidGradlePlugin = "com.android.tools.build:gradle:4.0.0-beta03"
    const val advancedVersioning = "org.moallemi.gradle.advanced-build-version:gradle-plugin:1.6.0"
    const val mvRx = "com.airbnb.android:mvrx:1.3.0"

    const val gravitySnapHelper = "com.github.rubensousa:gravitysnaphelper:2.2.0"
    const val jodaTime = "net.danlew:android.joda:2.9.9.3"
    const val gson = "com.google.code.gson:gson:2.8.5"

    const val junit = "junit:junit:4.12"
    const val robolectric = "org.robolectric:robolectric:4.3"
    const val mockK = "io.mockk:mockk:1.9.3"

    const val appauth = "net.openid:appauth:0.7.1"

    const val advancedDrawer = "com.infideap.drawerbehavior:drawer-behavior:0.2.2"
    const val ratingBar = "com.github.ome450901:SimpleRatingBar:1.4.2"

    object Google {
        const val material = "com.google.android.material:material:${Versions.material}"
        const val firebaseCore = "com.google.firebase:firebase-core:17.2.0"
        const val crashlytics = "com.crashlytics.sdk.android:crashlytics:2.10.1"
        const val gmsGoogleServices = "com.google.gms:google-services:4.3.2"
        const val fabricPlugin = "io.fabric.tools:gradle:1.31.1"
    }

    object Kotlin {
        private const val version = "1.3.60"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect:$version"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val extensions = "org.jetbrains.kotlin:kotlin-android-extensions:$version"
    }

    object Coroutines {
        private const val version = "1.3.2"
        const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$version"
        const val rx2 = "org.jetbrains.kotlinx:kotlinx-coroutines-rx2:$version"
        const val android = "org.jetbrains.kotlinx:kotlinx-coroutines-android:$version"
        const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$version"
    }

    object Insetter {
        private const val version = "0.2.0"
        const val dbx = "dev.chrisbanes:insetter-dbx:$version"
        const val ktx = "dev.chrisbanes:insetter-ktx:$version"
    }

    object AndroidX {
        const val appcompat = "androidx.appcompat:appcompat:1.1.0"
        const val browser = "androidx.browser:browser:1.2.0-beta01"
        const val collection = "androidx.collection:collection-ktx:1.1.0"
        const val palette = "androidx.palette:palette:1.0.0"
        const val recyclerview = "androidx.recyclerview:recyclerview:1.1.0-beta04"
        const val emoji = "androidx.emoji:emoji:1.0.0"
        const val swiperefresh = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0-alpha02"
        const val preference = "androidx.preference:preference:1.1.0"
        const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.0.0-beta3"
        const val coreKtx = "androidx.core:core-ktx:1.2.0-rc01"

        object Navigation {
            private const val version = "2.2.0-rc02"
            const val fragment = "androidx.navigation:navigation-fragment-ktx:$version"
            const val ui = "androidx.navigation:navigation-ui-ktx:$version"
            const val safeArgs = "androidx.navigation:navigation-safe-args-gradle-plugin:$version"
        }

        object Fragment {
            private const val version = "1.2.2"
            const val fragment = "androidx.fragment:fragment:$version"
            const val fragmentKtx = "androidx.fragment:fragment-ktx:$version"
        }

        object Test {
            private const val version = "1.2.0"
            const val core = "androidx.test:core:$version"
            const val runner = "androidx.test:runner:$version"
            const val rules = "androidx.test:rules:$version"

            const val espressoCore = "androidx.test.espresso:espresso-core:3.2.0"
            const val junit = "androidx.test.ext:junit:1.1.1"
            const val archCoreTesting = "androidx.arch.core:core-testing:2.1.0"
        }

        object Paging {
            private const val version = "2.1.0"
            const val common = "androidx.paging:paging-common-ktx:$version"
            const val runtime = "androidx.paging:paging-runtime-ktx:$version"
        }

        object Lifecycle {
            private const val version = "2.2.0-rc02"
            const val extensions = "androidx.lifecycle:lifecycle-extensions:$version"
            const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$version"
        }

        object Room {
            private const val version = "2.2.2"
            const val common = "androidx.room:room-common:$version"
            const val runtime = "androidx.room:room-runtime:$version"
            const val compiler = "androidx.room:room-compiler:$version"
            const val ktx = "androidx.room:room-ktx:$version"
            const val testing = "androidx.room:room-testing:$version"
        }

        object Work {
            private const val version = "2.2.0"
            const val runtimeKtx = "androidx.work:work-runtime-ktx:$version"
        }
    }

    object RxJava {
        const val rxJava = "io.reactivex.rxjava2:rxjava:2.2.11"
        const val rxAndroid = "io.reactivex.rxjava2:rxandroid:2.1.1"
    }

    object Dagger {
        private const val version = "2.24"
        const val dagger = "com.google.dagger:dagger:$version"
        const val androidSupport = "com.google.dagger:dagger-android-support:$version"
        const val compiler = "com.google.dagger:dagger-compiler:$version"
        const val androidProcessor = "com.google.dagger:dagger-android-processor:$version"
    }

    object Retrofit {
        private const val version = "2.6.2"
        const val retrofit = "com.squareup.retrofit2:retrofit:$version"
        const val retrofit_rxjava_adapter = "com.squareup.retrofit2:adapter-rxjava2:$version"
        const val gsonConverter = "com.squareup.retrofit2:converter-gson:$version"
    }

    object OkHttp {
        private const val version = "4.2.0"
        const val okhttp = "com.squareup.okhttp3:okhttp:$version"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$version"
    }

    object Glide {
        private const val version = "4.10.0"
        const val glide = "com.github.bumptech.glide:glide:$version"
        const val okHttpIntergation = "com.github.bumptech.glide:okhttp3-integration:$version"
        const val compiler = "com.github.bumptech.glide:compiler:$version"
    }

    object Epoxy {
        private const val version = "3.9.0"
        const val epoxy = "com.airbnb.android:epoxy:$version"
        const val paging = "com.airbnb.android:epoxy-paging:$version"
        const val dataBinding = "com.airbnb.android:epoxy-databinding:$version"
        const val processor = "com.airbnb.android:epoxy-processor:$version"
    }

    object ExoPlayer {
        private const val version = "2.9.6"
        const val player = "com.google.android.exoplayer:exoplayer:$version"
        const val mediaSession = "com.google.android.exoplayer:extension-mediasession:$version"
    }

    object AssistedInject {
        private const val version = "0.5.0"
        const val annotationDagger2 =
            "com.squareup.inject:assisted-inject-annotations-dagger2:$version"
        const val processorDagger2 =
            "com.squareup.inject:assisted-inject-processor-dagger2:$version"
    }

    object Roomigrant {
        private const val version = "0.1.7"
        const val library = "com.github.MatrixDev.Roomigrant:RoomigrantLib:$version"
        const val compiler = "com.github.MatrixDev.Roomigrant:RoomigrantCompiler:$version"
    }

    object Debug {
        const val leakCanary = "com.squareup.leakcanary:leakcanary-android:2.0-beta-3"
        const val dbDebug = "com.amitshekhar.android:debug-db:1.0.6"
    }
}