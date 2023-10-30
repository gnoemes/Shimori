plugins {
    id("multiplatform-ui-library")
}

android {
    namespace = "com.gnoemes.shimori.auth"
}

dependencies {
    commonMainImplementation(projects.domain)
    commonMainImplementation(projects.commonUi)
    commonMainImplementation(projects.commonUiResources)
    commonMainImplementation(projects.commonUiImageloading)
    commonMainImplementation(projects.shikimoriAuth)

    androidMainImplementation(androidx.core)
    androidMainImplementation(androidx.bundles.lifecycle)
    androidMainImplementation(androidx.navigation.compose)
    androidMainImplementation(platform(compose.bom))
    androidMainImplementation(compose.bundles.core)
    androidMainImplementation(compose.activity)
    androidMainImplementation(libs.kodein.compose)
}