package com.gnoemes.shimori.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.configurationcache.extensions.capitalized
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinPlatformType
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

class KotlinMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("org.jetbrains.kotlin.multiplatform")
        }

        extensions.configure<KotlinMultiplatformExtension> {
            applyDefaultHierarchyTemplate()

            jvm()
            if (pluginManager.hasPlugin("com.android.library")) {
                androidTarget()
            }

            listOf(
                iosX64(),
                iosArm64(),
                iosSimulatorArm64(),
            ).forEach { target ->
                target.binaries.framework {
                    baseName = path.substring(1).replace(':', '-')
                }
            }

            targets.withType<KotlinNativeTarget>().configureEach {
                binaries.all {
                    // Add linker flag for SQLite. See:
                    // https://github.com/touchlab/SQLiter/issues/77
                    linkerOpts("-lsqlite3")

                    // Workaround for https://youtrack.jetbrains.com/issue/KT-64508
                    freeCompilerArgs += "-Xdisable-phases=RemoveRedundantCallsToStaticInitializersPhase"
                }

                compilations.configureEach {
                    compileTaskProvider.configure {
                        compilerOptions {
                            // Various opt-ins
                            freeCompilerArgs.addAll(
                                "-opt-in=kotlinx.cinterop.ExperimentalForeignApi",
                                "-opt-in=kotlinx.cinterop.BetaInteropApi",
                            )
                        }
                    }
                }
            }

            targets.configureEach {
                compilations.configureEach {
                    compileTaskProvider.configure {
                        compilerOptions {
                            freeCompilerArgs.add("-Xexpect-actual-classes")
                        }
                    }
                }
            }

            configureKotlin()
        }
    }
}

fun Project.addKspDependencyForAllTargets(dependencyNotation: Any) =
    addKspDependencyForAllTargets("", dependencyNotation)

fun Project.addKspTestDependencyForAllTargets(dependencyNotation: Any) =
    addKspDependencyForAllTargets("Test", dependencyNotation)

private fun Project.addKspDependencyForAllTargets(
    configurationNameSuffix: String,
    dependencyNotation: Any,
) {
    val kmpExtension = extensions.getByType<KotlinMultiplatformExtension>()
    dependencies {
        kmpExtension.targets
            .asSequence()
            .filter { target ->
                // Don't add KSP for common target, only final platforms
                target.platformType != KotlinPlatformType.common
            }
            .forEach { target ->
                add(
                    "ksp${target.targetName.capitalized()}$configurationNameSuffix",
                    dependencyNotation,
                )
            }
    }
}

