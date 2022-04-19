import com.gnoemes.shimori.initConfigField

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
}

dependencies {
    implementation(projects.base.shared)
    implementation(projects.commonUi)
    implementation(projects.commonUiResources)
    implementation(projects.commonUiImageloading)
    implementation(projects.commonUiCompose.jetpack)

    implementation(projects.shikimori)

    implementation(projects.data)
    implementation(projects.data.db)

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

    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.accompanist.navigationanimation)
    implementation(libs.accompanist.navigationmaterial)

    implementation(libs.ktor.logging)
    implementation(libs.ktor.contentNegotiation)
    implementation(libs.ktor.serialization.json)
    implementation(libs.ktor.okhttp)
}