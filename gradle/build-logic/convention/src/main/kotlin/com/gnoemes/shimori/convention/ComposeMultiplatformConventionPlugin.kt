package com.gnoemes.shimori.convention

import com.gnoemes.shimori.compose
import org.gradle.api.Plugin
import org.gradle.api.Project

class ComposeMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.compose")
        configureCompose()
    }
}

fun Project.configureCompose() {
    val composeVersion = compose.findVersion("compose-multiplatform").get().requiredVersion

    configurations.configureEach {
        resolutionStrategy.eachDependency {
            val group = requested.group

            when {
                group.startsWith("org.jetbrains.compose") && !group.endsWith("compiler") -> {
                    useVersion(composeVersion)
                }
                // We need to force AndroidX Compose UI 1.6.0-alpha08 to be able to use new draw APIs
                group == "androidx.compose.ui" -> useVersion("1.6.0-alpha08")
            }
        }
    }
}
