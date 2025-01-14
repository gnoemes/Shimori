
dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }

    versionCatalogs {
        create("kotlinx") {
            from(files("../kotlin.versions.toml"))
        }
        create("androidx") {
            from(files("../androidx.versions.toml"))
        }
        create("composelibs") {
            from(files("../compose.versions.toml"))
        }
        create("libs") {
            from(files("../libs.versions.toml"))
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

include(":convention")