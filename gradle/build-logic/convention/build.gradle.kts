plugins {
    `kotlin-dsl`
    alias(kotlinx.plugins.detekt)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

detekt {
    source.setFrom(
        "src/main/kotlin",
        "src/main/java"
    )
}

dependencies {
    compileOnly(androidx.gradle)
    compileOnly(kotlinx.gradle)
    compileOnly(kotlinx.gradle.detekt)
    compileOnly(composelibs.gradle)
    compileOnly(kotlinx.gradle.compose.compiler)
    compileOnly(libs.google.appDistribution)
    compileOnly(libs.sqldelight.gradle)
}

gradlePlugin {
    plugins {
        register("kotlinMultiplatform") {
            id = "com.gnoemes.shimori.kotlin.multiplatform"
            implementationClass =
                "com.gnoemes.shimori.convention.multiplatform.KotlinMultiplatformConventionPlugin"
        }

        register("kotlinMultiplatformCommon") {
            id = "com.gnoemes.shimori.kotlin.multiplatform.common"
            implementationClass =
                "com.gnoemes.shimori.convention.multiplatform.KotlinMultiplatformCommonConventionPlugin"
        }

        register("kotlinMultiplatformData") {
            id = "com.gnoemes.shimori.kotlin.multiplatform.data"
            implementationClass =
                "com.gnoemes.shimori.convention.multiplatform.KotlinMultiplatformDataConventionPlugin"
        }

        register("androidApplication") {
            id = "com.gnoemes.shimori.android.application"
            implementationClass =
                "com.gnoemes.shimori.convention.android.AndroidApplicationConventionPlugin"
        }

        register("androidLibrary") {
            id = "com.gnoemes.shimori.android.library"
            implementationClass =
                "com.gnoemes.shimori.convention.android.AndroidLibraryConventionPlugin"
        }

        register("androidTest") {
            id = "com.gnoemes.shimori.android.test"
            implementationClass =
                "com.gnoemes.shimori.convention.android.AndroidTestConventionPlugin"
        }

        register("compose") {
            id = "com.gnoemes.shimori.compose"
            implementationClass =
                "com.gnoemes.shimori.convention.multiplatform.ComposeMultiplatformConventionPlugin"
        }

        register("composeFeature") {
            id = "com.gnoemes.shimori.compose.feature"
            implementationClass =
                "com.gnoemes.shimori.convention.multiplatform.ComposeMultiplatformFeatureConventionPlugin"
        }
    }
}
