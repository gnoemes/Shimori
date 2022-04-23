import com.android.build.api.dsl.LibraryExtension

plugins {
    id("multiplatform-library")
}

//https://youtrack.jetbrains.com/issue/KT-38694
//workaround (https://github.com/arunkumar9t2/compose_mpp_workaround/tree/patch-1):
configurations {
    create("composeCompiler") {
        isCanBeConsumed = false
    }
}

dependencies {
    "composeCompiler"("androidx.compose.compiler:compiler:1.2.0-alpha08")
}

android {
    afterEvaluate {
        val composeCompilerJar =
            configurations["composeCompiler"]
                .resolve()
                .singleOrNull()
                ?: error("Please add \"androidx.compose:compose-compiler\" (and only that) as a \"composeCompiler\" dependency")
        tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
            kotlinOptions.freeCompilerArgs += listOf("-Xuse-ir", "-Xplugin=$composeCompilerJar")
        }
    }
}