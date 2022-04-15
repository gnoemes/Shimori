plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    api("io.gitlab.arturbosch.detekt:detekt-gradle-plugin:1.20.0-RC2")
    api("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
    api("com.android.tools.build:gradle:7.3.0-alpha08")
}