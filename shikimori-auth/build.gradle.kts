plugins {
    id("multiplatform-library")
}

android {
    namespace = "com.gnoemes.shimori.shikimori.auth"
}

dependencies {
    commonMainApi(projects.shikimori)
    commonMainImplementation(projects.base.shared)

    androidMainImplementation(androidx.browser)
    androidMainImplementation(androidx.core)
    androidMainImplementation(compose.activity)
}