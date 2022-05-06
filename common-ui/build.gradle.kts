plugins {
    id("multiplatform-ui-library")
}

android {
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = libs.versions.compose.get()
}

dependencies {
    commonMainApi(projects.base.shared)
    commonMainImplementation(projects.data.core)
    commonMainImplementation(projects.commonUiResources)
    commonMainImplementation(projects.commonUiImageloading)

    androidMainImplementation(libs.androidx.fragment)
    androidMainImplementation(libs.androidx.core)
    androidMainImplementation(libs.androidx.lifecycle.runtime)
    androidMainImplementation(libs.androidx.lifecycle.viewmodel.ktx)
    androidMainImplementation(libs.androidx.lifecycle.viewmodel.compose)
    androidMainImplementation(libs.compose.foundation.foundation)
    androidMainImplementation(libs.compose.foundation.layout)
    androidMainImplementation(libs.compose.material.material3)
    androidMainImplementation(libs.compose.material.material)
    androidMainImplementation(libs.compose.animation.animation)
    androidMainImplementation(libs.compose.ui.util)
    androidMainImplementation(libs.compose.ui.tooling)
    androidMainImplementation(libs.compose.ui.ui)
    androidMainImplementation(libs.kodein.compose)
    androidMainImplementation(libs.accompanist.navigationmaterial)
}