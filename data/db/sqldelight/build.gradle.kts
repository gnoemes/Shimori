import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    id("com.gnoemes.shimori.android.library")
    id("com.gnoemes.shimori.kotlin.multiplatform")
    alias(libs.plugins.sqldelight)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(projects.core.base)
                implementation(projects.core.logging.api)

                api(projects.data.db.api)
                api(kotlinx.dateTime)
                // Need to force upgrade these for recent Kotlin support
                api(kotlinx.atomicfu)
                api(kotlinx.coroutines.core)

                implementation(libs.kotlininject.runtime)

                implementation(libs.sqldelight.coroutines)
                implementation(libs.sqldelight.paging)
                implementation(libs.sqldelight.primitive)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(libs.sqldelight.android)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(libs.sqldelight.sqlite)
            }
        }

        val iosMain by getting {
            dependencies {
                implementation(libs.sqldelight.native)
            }
        }
    }
}

sqldelight {
    databases {
        create("ShimoriDB") {
            packageName = "com.gnoemes.shimori.data"
            dialect(libs.sqldelight.dialects.sql)
        }
    }
}

tasks.withType<KotlinCompilationTask<*>>().configureEach {
    compilerOptions {
        // Have to disable this as some of the generated code has
        // warnings for unused parameters
        allWarningsAsErrors = false
    }
}

android {
    namespace = "com.gnoemes.shimori.data.sqldelight"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
}