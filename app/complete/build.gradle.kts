import com.gnoemes.shimori.convention.core.addKspDependencyForAllTargets

plugins {
    alias(libs.plugins.shimori.kotlin.multiplatform.common)
    alias(libs.plugins.shimori.compose)
    alias(kotlinx.plugins.ksp)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.app.core)
            }
        }
    }
}

ksp {
    arg("me.tatarka.inject.generateCompanionExtensions", "true")
}

addKspDependencyForAllTargets(libs.kotlininject.compiler)
