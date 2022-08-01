import com.android.build.api.dsl.LibraryExtension

plugins {
    id("multiplatform-ui-library")
}

configure<LibraryExtension>() {
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = libs.versions.compose.get()
}

dependencies {
    commonMainImplementation(projects.domain)
    commonMainImplementation(projects.commonUi)
    commonMainImplementation(projects.commonUiResources)
    commonMainImplementation(projects.commonUiImageloading)
    commonMainImplementation(projects.shikimoriAuth)

    androidMainImplementation(libs.androidx.core)
    androidMainImplementation(libs.androidx.lifecycle.runtime)
    androidMainImplementation(libs.androidx.lifecycle.compose)
    androidMainImplementation(libs.androidx.lifecycle.viewmodel.ktx)
    androidMainImplementation(libs.androidx.lifecycle.viewmodel.compose)
    androidMainImplementation(libs.androidx.activity.compose)
    androidMainImplementation(libs.androidx.navigation.compose)
    androidMainImplementation(libs.compose.foundation.foundation)
    androidMainImplementation(libs.compose.foundation.layout)
    androidMainImplementation(libs.compose.material.material3)
    androidMainImplementation(libs.compose.material.material3.windowsizeclass)
    androidMainImplementation(libs.compose.material.material)
    androidMainImplementation(libs.compose.animation.animation)
    androidMainImplementation(libs.compose.ui.util)
    androidMainImplementation(libs.compose.ui.tooling)
    androidMainImplementation(libs.compose.ui.ui)
    androidMainImplementation(libs.kodein.compose)
}