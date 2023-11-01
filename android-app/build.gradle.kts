@file:Suppress("UnstableApiUsage")

import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.gnoemes.shimori.initConfigField
import com.gnoemes.shimori.propOrDef
import com.gnoemes.shimori.readVersion

plugins {
    id("com.android.application")
    id("android-android-app")
    kotlin("plugin.serialization")
}

android {
    namespace = "com.gnoemes.shimori"

    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions.kotlinCompilerExtensionVersion = compose.versions.composeCompiler.get()

    val version = readVersion("${project.projectDir}/version.properties")

    val versionMajor = version["major"].toString().toInt()
    val versionMinor = version["minor"].toString().toInt()
    val versionPatch = version["patch"].toString().toInt()

    val appVersionName = "$versionMajor.$versionMinor.$versionPatch"
    val appVersionCode = version["VERSION_CODE"].toString().toIntOrNull()

    println("config code: $appVersionCode, name: $appVersionName")

    defaultConfig {
        versionCode = appVersionCode
        versionName = appVersionName
        setProperty("archivesBaseName", "Shimori-v$appVersionName($appVersionCode)")

        with(project) {
            initConfigField(this@defaultConfig, "ShikimoriClientId", "none")
            initConfigField(this@defaultConfig, "ShikimoriClientSecret", "none")
            initConfigField(this@defaultConfig, "ShikimoriBaseUrl")
        }
    }

    val useReleaseKeystore = rootProject.file("release/android-app-release.jks").exists()

    signingConfigs {
        getByName("debug") {
            storeFile = rootProject.file("release/android-app-debug.jks")
            storePassword = "password"
            keyAlias = "debugkey"
            keyPassword = "password"
        }

        create("release") {
            if (useReleaseKeystore) {
                storeFile = rootProject.file("release/android-app-release.jks")
                storePassword = propOrDef("ReleaseStorePassword", "").toString()
                keyAlias = "shimori"
                keyPassword = propOrDef("ReleaseKeyPassword", "").toString()
            }
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig =
                if (useReleaseKeystore) signingConfigs.getByName("release")
                else signingConfigs.getByName("debug")

            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-dev"
        }
        create("qa") {
            matchingFallbacks.add("release")
            isDebuggable = true
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("debug")
            versionNameSuffix = "-qa"
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation(projects.base.shared)
    implementation(projects.commonUi)
    implementation(projects.commonUiResources)
    implementation(projects.commonUiImageloading)

    implementation(projects.data)
    implementation(projects.data.db)
    implementation(projects.sourceApi)
    implementation(projects.domain)
    implementation(projects.tasks)

    implementation(projects.shikimori)
    implementation(projects.shikimoriAuth)

    implementation(projects.ui.home)
    implementation(projects.ui.auth)
    implementation(projects.ui.lists)
    implementation(projects.ui.lists.menu)
    implementation(projects.ui.lists.edit)
    implementation(projects.ui.settings)
    implementation(projects.ui.title)

    implementation(kotlinx.coroutines.android)

    implementation(androidx.splashscreen)
    implementation(androidx.datastore)
    implementation(androidx.bundles.lifecycle)
    implementation(androidx.fragment)
    implementation(androidx.navigation.compose)

    implementation(platform(compose.bom))
    implementation(compose.activity)
    implementation(compose.bundles.core)

    implementation(compose.accompanist.systemuicontroller)
    implementation(compose.accompanist.navigationmaterial)
    implementation(compose.accompanist.placeholder)
    implementation(compose.accompanist.webview)

    implementation(libs.coil.compose)
    implementation(libs.kodein.compose)

    implementation(libs.material.motion.navigation)
    implementation(libs.bundles.voyager)

    implementation(libs.ktor.logging)
    implementation(libs.ktor.contentNegotiation)
    implementation(libs.ktor.serialization.json)
    implementation(libs.ktor.okhttp)
    implementation(libs.ktor.auth)

    implementation(libs.google.analytics)
    implementation(libs.google.crashlytics)

    implementation(libs.slf4j)
}

val firebaseAppId = project.propOrDef("firebaseAppId", "").toString()
if (firebaseAppId.isNotEmpty()) {
    apply(plugin = "com.google.firebase.appdistribution")

    configure<BaseAppModuleExtension> {
        buildTypes {
            getByName("release") {
                configure<com.google.firebase.appdistribution.gradle.AppDistributionExtension> {
                    appId = firebaseAppId
                    groups = "qa"
                    artifactType = "APK"
                }
            }
        }
    }
}

if (file("google-services.json").exists()) {
    apply(plugin = "com.google.gms.google-services")
    apply(plugin = "com.google.firebase.crashlytics")
}
