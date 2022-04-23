import com.android.build.api.dsl.LibraryExtension
import com.gnoemes.shimori.initConfigField

plugins {
    id("multiplatform-library")
}

dependencies {
    commonMainApi(projects.shikimori)
    commonMainImplementation(projects.base.shared)

    androidMainImplementation(libs.androidx.browser)
    androidMainImplementation(libs.androidx.core)
    androidMainImplementation(libs.androidx.activity.compose)
    androidMainImplementation(libs.appauth)
}