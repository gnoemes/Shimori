package com.gnoemes.shimori.convention.ksp

import com.gnoemes.shimori.convention.ProjectConfig
import com.gnoemes.shimori.convention.core.addKspDependencyForAllTargets
import com.gnoemes.shimori.convention.libs
import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class KspKotlinInjectAnvilFeatureConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("${ProjectConfig.APP_PACKAGE}.ksp.anvil")

        extensions.configure<KspExtension> {
            arg("circuit.codegen.mode", "kotlin_inject_anvil")
            arg("circuit.codegen.lenient", "true")
            arg("kotlin-inject-anvil-contributing-annotations", "com.slack.circuit.codegen.annotations.CircuitInject")
        }

        addKspDependencyForAllTargets(libs.findLibrary("circuit.codegen").get())
    }
}