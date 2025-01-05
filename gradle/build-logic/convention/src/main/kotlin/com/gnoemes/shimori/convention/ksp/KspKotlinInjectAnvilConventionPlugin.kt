package com.gnoemes.shimori.convention.ksp

import com.gnoemes.shimori.convention.core.addKspDependencyForAllTargets
import com.gnoemes.shimori.convention.libs
import com.google.devtools.ksp.gradle.KspExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class KspKotlinInjectAnvilConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        pluginManager.apply("com.google.devtools.ksp")

        extensions.configure<KspExtension> {
            arg("me.tatarka.inject.generateCompanionExtensions", "true")
        }

        addKspDependencyForAllTargets(libs.findLibrary("kotlininject.anvil.compiler").get())
    }
}