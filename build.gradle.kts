plugins {
    alias(kotlinx.plugins.detekt) apply false
    alias(androidx.plugins.android.application) apply false
    alias(androidx.plugins.android.library) apply false
    alias(androidx.plugins.android.lint) apply false
    alias(androidx.plugins.android.test) apply false
    alias(androidx.plugins.cacheFixPlugin) apply false
    alias(kotlinx.plugins.android) apply false
    alias(kotlinx.plugins.serialization) apply false
    alias(kotlinx.plugins.parcelize) apply false
    alias(libs.plugins.google.gmsGoogleServices) apply false
    alias(libs.plugins.google.crashlytics) apply false
    alias(libs.plugins.google.appDistribution) apply false
    alias(composelibs.plugins.multiplatform) apply false
    alias(kotlinx.plugins.compose.compiler) apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}