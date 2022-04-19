plugins {
    id("base-module")
    id("kotlin")
    kotlin("plugin.serialization")
}

dependencies {
    implementation(projects.base.core)

    implementation(libs.kotlin.dateTime)
}