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

        // Used for snapshots if needed
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
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
        create("composelibs") {
            from(files("gradle/compose.versions.toml"))
        }
    }

    repositories {
        mavenCentral()
        google()
        mavenLocal()

        // Prerelease versions of Compose Multiplatform
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")

        // Used for snapshots if needed
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

rootProject.name = "Shimori"

include(
    ":core:base",
    ":core:settings",
    ":core:preferences",
    ":core:logging:api",
    ":core:logging:impl",
    ":common:ui:compose",
    ":data:models",
    ":data:db:api",
    ":data:db:sqldelight",
    ":app:core",
    ":app:complete",
    ":android-app",
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
    ":source-api:core",
    ":source-api:catalogue",
    ":source-api:track",
    )
