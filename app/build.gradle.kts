import com.gnoemes.shimori.initConfigField

plugins {
    id("android-app")
}

android {
    buildFeatures {
        compose = true
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
    implementation(libs.androidx.core)

    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.extensions)
    implementation(libs.kotlin.coroutines.core)
    implementation(libs.kotlin.coroutines.android)

    implementation(libs.compose.ui.ui)
    implementation(libs.compose.ui.tooling)
    implementation(libs.compose.ui.viewbinding)
    implementation(libs.compose.foundation.layout)
    implementation(libs.compose.foundation.foundation)
    implementation(libs.compose.material.material3)
    implementation(libs.compose.animation.animation)
}