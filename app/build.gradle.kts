import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.gnoemes.shimori.initConfigField
import com.gnoemes.shimori.propOrDef

plugins {
    id("com.android.application")
    id("android-app")
}

android {
    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.get()
    }

    defaultConfig {
        applicationId = com.gnoemes.shimori.Application.id
        versionCode = 1
        versionName = "1.0.0"

        with(project) {
            initConfigField(this@defaultConfig, "ShikimoriClientId", "none")
            initConfigField(this@defaultConfig, "ShikimoriClientSecret", "none")
            initConfigField(this@defaultConfig, "ShikimoriBaseUrl")
        }
    }

    val useReleaseKeystore = rootProject.file("release/app-release.jks").exists()

    signingConfigs {
        getByName("debug") {
            storeFile = rootProject.file("release/app-debug.jks")
            storePassword = "password"
            keyAlias = "debugkey"
            keyPassword = "password"
        }

        create("release") {
            if (useReleaseKeystore) {
                storeFile = rootProject.file("release/app-release.jks")
                storePassword = propOrDef("ReleaseStorePassword", "").toString()
                keyAlias = "shimori"
                keyPassword = propOrDef("ReleaseKeyPassword", "").toString()
            }
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = if (useReleaseKeystore) {
                signingConfigs.getByName("release")
            } else {
                signingConfigs.getByName("debug")
            }

            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        getByName("debug") {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-dev"
        }
    }

}

dependencies {
    implementation(projects.base.shared)
    implementation(projects.commonUi)
    implementation(projects.commonUiResources)
    implementation(projects.commonUiImageloading)
    implementation(projects.commonUiCompose.jetpack)

    implementation(projects.data)
    implementation(projects.data.db)
    implementation(projects.domain)

    implementation(projects.shikimori)

    implementation(libs.kotlin.coroutines.android)

    implementation(libs.androidx.splashscreen)
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.compose.foundation.foundation)
    implementation(libs.compose.foundation.layout)
    implementation(libs.compose.material.material3)
    implementation(libs.compose.material.material)
    implementation(libs.compose.animation.animation)
    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.viewbinding)
    implementation(libs.compose.ui.util)
    implementation(libs.compose.ui.ui)

    implementation(libs.coil.compose)
    implementation(libs.kodein.compose)

    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.accompanist.navigationanimation)
    implementation(libs.accompanist.navigationmaterial)

    implementation(libs.ktor.logging)
    implementation(libs.ktor.contentNegotiation)
    implementation(libs.ktor.serialization.json)
    implementation(libs.ktor.okhttp)

    implementation(libs.google.analytics)
    implementation(libs.google.crashlytics)
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
