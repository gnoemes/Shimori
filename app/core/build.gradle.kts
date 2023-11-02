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
                api(projects.core.base)
                api(projects.core.logging.impl)
                api(projects.core.settings)
                api(projects.core.preferences)
            }
        }

        val androidMain by getting {
            dependencies {
                implementation(androidx.core)
            }
        }
    }
}

android {
    namespace = "com.gnoemes.shimori.app.core"
}

ksp {
    arg("me.tatarka.inject.generateCompanionExtensions", "true")
}

addKspDependencyForAllTargets(libs.kotlininject.compiler)