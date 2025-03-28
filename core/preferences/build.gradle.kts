plugins {
    alias(libs.plugins.shimori.kotlin.multiplatform.core)
    alias(libs.plugins.shimori.ksp.anvil)
}


kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.core.settings)
                api(libs.multiplatform.settings.core)
                api(libs.multiplatform.settings.coroutines)

                implementation(projects.core.base)
            }
        }

        androidMain {
            dependencies {
                implementation(androidx.core)
            }
        }
    }
}