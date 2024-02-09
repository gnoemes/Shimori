import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

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

    //TODO migrate to Compose Resources
    //Compose resources doesn't support multi module projects right now. Making common:ui:resources basically useless
    //https://github.com/JetBrains/compose-multiplatform/issues/4083
    //Temporary replacing this functionality with lib
    id("dev.icerock.mobile.multiplatform-resources") version "0.24.0-alpha-3" apply false
}

buildscript {
    dependencies {
        // https://github.com/gmazzo/gradle-buildconfig-plugin/issues/131
        // Yuck. Need to force kotlinpoet:1.16.0 as that is what buildconfig uses.
        // CMP 1.6.0-x uses kotlinpoet:1.14.x. Gradle seems to force 1.14.x which then breaks
        // buildconfig.
        classpath("com.squareup:kotlinpoet:1.16.0")
    }
}

allprojects {
    tasks.withType<KotlinCompilationTask<*>>().configureEach {
        compilerOptions {
            // Treat all Kotlin warnings as errors
            allWarningsAsErrors = true
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}