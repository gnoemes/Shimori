package com.gnoemes.shimori.convention.multiplatform

import com.gnoemes.shimori.convention.ProjectConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KotlinMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("org.jetbrains.kotlin.multiplatform")
            apply("${ProjectConfig.APP_PACKAGE}.android.library")
        }

        extensions.configure<KotlinMultiplatformExtension> {
            applyDefaultHierarchyTemplate()

            jvm()
            androidTarget()

            targets.configureEach {
                compilations.configureEach {
                    compileTaskProvider.configure {
                        compilerOptions {
                            freeCompilerArgs.add("-Xexpect-actual-classes")
                        }
                    }
                }
            }


        }
    }
}
