enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
enableFeaturePreview("STABLE_CONFIGURATION_CACHE")

pluginManagement {
    includeBuild("gradle/build-logic")

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()

        // Prerelease versions of Compose Multiplatform
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

dependencyResolutionManagement {
    versionCatalogs {
        create("kotlinx") {
            from(files("gradle/kotlin.versions.toml"))
        }
        create("androidx") {
            from(files("gradle/androidx.versions.toml"))
        }
        create("compose") {
            from(files("gradle/compose.versions.toml"))
        }
    }

    repositories {
        mavenCentral()
        google()
        mavenLocal()

        // Prerelease versions of Compose Multiplatform
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

rootProject.name = "Shimori"

include(
//    ":android-app",
    ":core:base",
    ":core:settings",
    ":core:preferences",
    ":core:logging:api",
    ":core:logging:impl"
//    ":core:logging"
//    ":base:shared",
//    ":common-ui",
//    ":common-ui-resources",
//    ":common-ui-imageloading",
//    ":data",
//    ":data:core",
//    ":data:db",
//    ":data:paging",
//    ":domain",
//    ":tasks",
//    ":shikimori",
//    ":shikimori-auth",
//    ":ui:home",
//    ":ui:auth",
//    ":ui:lists",
//    ":ui:lists:menu",
//    ":ui:lists:edit",
//    ":ui:settings",
//    ":ui:title",
//
//    ":source-api"
    )