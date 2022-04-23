plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    api("dev.icerock:mobile-multiplatform:0.14.1")
    api("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.20.0-RC2")
    api("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.20")
    api("com.android.tools.build:gradle:7.3.0-alpha08")
}