package com.gnoemes.shimori.convention.multiplatform

import com.gnoemes.shimori.convention.core.configureCompose
import org.gradle.api.Plugin
import org.gradle.api.Project

class ComposeMultiplatformConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("org.jetbrains.compose")
        pluginManager.apply("org.jetbrains.kotlin.plugin.compose")
        configureCompose()
    }
}