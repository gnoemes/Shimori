import com.gnoemes.shimori.convention.ProjectConfig
import org.jetbrains.kotlin.gradle.tasks.KotlinCompilationTask

plugins {
    alias(libs.plugins.shimori.kotlin.multiplatform.common)
    alias(libs.plugins.sqldelight)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.data.db.api)
                api(projects.data.models)
                // Need to force upgrade these for recent Kotlin support
                api(kotlinx.atomicfu)
                api(kotlinx.coroutines.core)

                implementation(libs.sqldelight.coroutines)
                implementation(libs.sqldelight.paging)
                implementation(libs.sqldelight.primitive)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.sqldelight.android)
            }
        }

        jvmMain {
            dependencies {
                implementation(libs.sqldelight.sqlite)
            }
        }
    }
}

sqldelight {
    databases {
        create("ShimoriDB") {
            packageName = "${ProjectConfig.APP_PACKAGE}.data"
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