plugins {
    id("multiplatform-ui-library")
}

dependencies {
    commonMainApi(projects.base.shared)
    commonMainImplementation(projects.data.core)
    commonMainImplementation(projects.commonUiResources)
    commonMainImplementation(projects.commonUiImageloading)
    commonMainApi(libs.bundles.voyager)
    commonMainImplementation(libs.uuid)

    androidMainImplementation(androidx.fragment)
    androidMainImplementation(androidx.core)
    androidMainImplementation(androidx.bundles.lifecycle)
    androidMainImplementation(androidx.paging.common)
    androidMainImplementation(androidx.paging.compose)
    androidMainImplementation(androidx.palette)
    androidMainImplementation(platform(compose.bom))
    androidMainImplementation(compose.bundles.core)
    androidMainImplementation(compose.accompanist.navigationmaterial)
    androidMainImplementation(compose.accompanist.placeholder)
    androidMainImplementation(libs.kodein.compose)
    androidMainImplementation(libs.extendedGestures)

}