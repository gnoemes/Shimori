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

rootProject.name = "Shimori"

include(":app")

include(":base:core", ":base:shared")
include(":common-ui", ":common-ui-resources", ":common-ui-imageloading")

include(":data", ":data:core", ":data:db", ":data:paging")

include(":domain")
include(":tasks")

include(":shikimori", ":shikimori-auth")

include(":ui:auth")
include(":ui:lists", ":ui:lists:change", ":ui:lists:edit")
include(":ui:settings")
