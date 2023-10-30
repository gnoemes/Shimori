
dependencyResolutionManagement {
    versionCatalogs {
        create("kotlinx") {
            from(files("../kotlin.versions.toml"))
        }
        create("androidx") {
            from(files("../androidx.versions.toml"))
        }
        create("compose") {
            from(files("../compose.versions.toml"))
        }
        create("libs") {
            from(files("../libs.versions.toml"))
        }
    }
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        google()
        maven(url = "https://www.jitpack.io")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

include(":convention")