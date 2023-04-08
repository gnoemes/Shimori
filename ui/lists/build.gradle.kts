plugins {
    id("multiplatform-ui-library")
}

dependencies {
    commonMainImplementation(projects.domain)
    commonMainImplementation(projects.commonUi)
    commonMainImplementation(projects.commonUiResources)
    commonMainImplementation(projects.commonUiImageloading)

    androidMainImplementation(androidx.core)
    androidMainImplementation(androidx.bundles.lifecycle)
    androidMainImplementation(androidx.paging.common)
    androidMainImplementation(androidx.paging.compose)
    androidMainImplementation(androidx.navigation.compose)
    androidMainImplementation(platform(compose.bom))
    androidMainImplementation(compose.bundles.core)
    androidMainImplementation(compose.activity)
    androidMainImplementation(compose.accompanist.navigationmaterial)
    androidMainImplementation(compose.accompanist.placeholder)
    androidMainImplementation(compose.accompanist.flowlayout)
    androidMainImplementation(libs.extendedGestures)
}