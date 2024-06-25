import com.gnoemes.shimori.convention.addKspDependencyForAllTargets

plugins {
    id("com.gnoemes.shimori.android.library")
    id("com.gnoemes.shimori.kotlin.multiplatform")
    id("com.gnoemes.shimori.compose")
    alias(kotlinx.plugins.ksp)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.app.core)
            }
        }

        androidMain {
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
