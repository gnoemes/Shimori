import com.gnoemes.shimori.convention.addKspDependencyForAllTargets

plugins {
    id("com.gnoemes.shimori.android.library")
    id("com.gnoemes.shimori.kotlin.multiplatform")
    id("com.gnoemes.shimori.compose")
    alias(kotlinx.plugins.ksp)
}

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(projects.app.core)
            }
        }

        val androidMain by getting {
            dependencies {

            }
        }
    }
}

android {
    namespace = "com.gnoemes.shimori.app.complete"
}

ksp {
    arg("me.tatarka.inject.generateCompanionExtensions", "true")
}

addKspDependencyForAllTargets(libs.kotlininject.compiler)
