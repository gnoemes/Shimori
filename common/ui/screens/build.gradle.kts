import com.gnoemes.shimori.convention.ProjectConfig
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType

plugins {
    alias(libs.plugins.shimori.kotlin.multiplatform.common)
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
                            "plugin:org.jetbrains.kotlin.parcelize:additionalAnnotation=${ProjectConfig.APP_PACKAGE}.screens.Parcelize",
                        )
                    }
                }
            }
        }
    }
}