import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

plugins {
    id("com.gnoemes.shimori.android.library")
    id("com.gnoemes.shimori.kotlin.multiplatform")
    alias(kotlinx.plugins.parcelize)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(libs.circuit.runtime)
            }
        }
    }

    targets.configureEach {
        val isAndroidTarget = platformType == KotlinPlatformType.androidJvm
        compilations.configureEach {
            compileTaskProvider.configure {
                compilerOptions {
                    if (isAndroidTarget) {
                        freeCompilerArgs.addAll(
                            "-P",
                            "plugin:org.jetbrains.kotlin.parcelize:additionalAnnotation=com.gnoemes.shimori.screens.Parcelize",
                        )
                    }
                }
            }
        }
    }
}

android {
    namespace = "com.gnoemes.shimori.screens"
}