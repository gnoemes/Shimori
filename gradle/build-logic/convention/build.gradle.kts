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
    compileOnly(libs.google.appDistribution)
    compileOnly(libs.sqldelight.gradle)
}

gradlePlugin {
    plugins {
        register("kotlinMultiplatform") {
            id = "com.gnoemes.shimori.kotlin.multiplatform"
            implementationClass = "com.gnoemes.shimori.convention.KotlinMultiplatformConventionPlugin"
        }

        register("kotlinAndroid") {
            id = "com.gnoemes.shimori.kotlin.android"
            implementationClass = "com.gnoemes.shimori.convention.KotlinAndroidConventionPlugin"
        }

        register("androidApplication") {
            id = "com.gnoemes.shimori.android.application"
            implementationClass = "com.gnoemes.shimori.convention.AndroidApplicationConventionPlugin"
        }

        register("androidLibrary") {
            id = "com.gnoemes.shimori.android.library"
            implementationClass = "com.gnoemes.shimori.convention.AndroidLibraryConventionPlugin"
        }

        register("androidTest") {
            id = "com.gnoemes.shimori.android.test"
            implementationClass = "com.gnoemes.shimori.convention.AndroidTestConventionPlugin"
        }

        register("compose") {
            id = "com.gnoemes.shimori.compose"
            implementationClass = "com.gnoemes.shimori.convention.ComposeMultiplatformConventionPlugin"
        }
    }
}
