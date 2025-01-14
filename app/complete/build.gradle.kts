import com.gnoemes.shimori.convention.core.addKspDependencyForAllTargets

plugins {
    alias(libs.plugins.shimori.kotlin.multiplatform.common)
    alias(libs.plugins.shimori.compose)
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
    //kotlin-inject-anvil component generation is bugged for child components
    arg("me.tatarka.inject.generateCompanionExtensions", "false")
}

addKspDependencyForAllTargets(libs.kotlininject.compiler)
