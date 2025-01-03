import com.gnoemes.shimori.convention.propOrDef

plugins {
    id("com.gnoemes.shimori.kotlin.multiplatform")
    alias(libs.plugins.buildConfig)
}

buildConfig {
    packageName("com.gnoemes.shimori.sources.shikimori")

    buildConfigField(
        type = "String",
        name = "ShikimoriClientId",
        value = "${propOrDef("ShikimoriClientId", "none")}",
    )
    buildConfigField(
        type = "String",
        name = "ShikimoriClientSecret",
        value = "${propOrDef("ShikimoriClientSecret", "none")}",
    )
    buildConfigField(
        type = "String",
        name = "ShikimoriBaseUrl",
        value = "${propOrDef("ShikimoriBaseUrl", "none")}",
    )
}


kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.base)
                implementation(projects.data.models)
                implementation(libs.kotlininject.runtime)
            }
        }
    }
}
