enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "kotlin-multiplatform") {
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${requested.version}")
            }
            if (requested.id.id == "com.android.library") {
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${requested.version}")
            }
            if (requested.id.id == "kotlinx-serialization") {
                useModule("org.jetbrains.kotlin:kotlin-serialization:${requested.version}")
            }
        }
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
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        google()
        maven(url = "https://www.jitpack.io")
    }
}

rootProject.name = "Shimori"

include(":app")

include(":base:core", ":base:shared")
include(":common-ui", ":common-ui-resources", ":common-ui-imageloading")

include(":data", ":data:core", ":data:db", ":data:paging")

include(":domain")
include(":tasks")

include(":shikimori", ":shikimori-auth")

include(":ui:home")
include(":ui:auth")
include(":ui:lists", ":ui:lists:change", ":ui:lists:edit")
include(":ui:settings")

include(":ui:title")
include(":source-api")
