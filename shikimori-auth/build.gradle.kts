plugins {
    id("multiplatform-library")
}

dependencies {
    commonMainApi(projects.shikimori)
    commonMainImplementation(projects.base.shared)

    androidMainImplementation(androidx.browser)
    androidMainImplementation(androidx.core)
    androidMainImplementation(compose.activity)
}