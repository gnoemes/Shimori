plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    api(libs.gradle.mobile.multiplatform)
    api(libs.gradle.detekt)
    api(kotlinLibs.gradle)
    api(androidxLibs.gradle)
}