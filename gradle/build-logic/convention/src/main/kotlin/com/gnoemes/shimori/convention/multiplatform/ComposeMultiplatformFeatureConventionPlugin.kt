package com.gnoemes.shimori.convention.multiplatform

import com.gnoemes.shimori.convention.ProjectConfig
import com.gnoemes.shimori.convention.androidx
import com.gnoemes.shimori.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class ComposeMultiplatformFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("${ProjectConfig.APP_PACKAGE}.kotlin.multiplatform")
            apply("${ProjectConfig.APP_PACKAGE}.kotlin.multiplatform.common")
            apply("${ProjectConfig.APP_PACKAGE}.compose")
            apply("${ProjectConfig.APP_PACKAGE}.ksp.anvil.feature")
        }

        extensions.configure<KotlinMultiplatformExtension>() {
            sourceSets.apply {
                commonMain {
                    dependencies {
                        implementation(project(":common:ui:compose"))
                        implementation(project(":common:ui:screens"))
                        implementation(project(":common:ui:overlay"))
                        implementation(project(":common:imageloading"))

                        implementation(project(":domain"))

                        implementation(libs.findLibrary("circuit.foundation").get())
                        implementation(libs.findLibrary("circuit.retained").get())
                        implementation(libs.findLibrary("circuit.gestureNavigation").get())
                        implementation(libs.findLibrary("circuit.overlay").get())
                    }
                }

                androidMain {
                    dependencies {
                        implementation(androidx.findLibrary("activity.compose").get())
                    }
                }
            }
        }
    }
}