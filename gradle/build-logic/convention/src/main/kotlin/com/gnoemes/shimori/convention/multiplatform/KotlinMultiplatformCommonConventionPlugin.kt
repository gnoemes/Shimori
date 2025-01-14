package com.gnoemes.shimori.convention.multiplatform

import com.gnoemes.shimori.convention.ProjectConfig
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KotlinMultiplatformCommonConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("${ProjectConfig.APP_PACKAGE}.kotlin.multiplatform")
            apply("${ProjectConfig.APP_PACKAGE}.ksp.anvil")
        }

        extensions.configure<KotlinMultiplatformExtension>() {
            sourceSets.apply {
                commonMain {
                    dependencies {
                        implementation(project(":core:base"))
                        implementation(project(":core:preferences"))
                    }
                }
            }
        }
    }
}