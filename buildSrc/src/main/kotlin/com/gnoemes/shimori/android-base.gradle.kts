import com.android.build.gradle.internal.dsl.BaseAppModuleExtension

plugins {
    id("base-module")
    kotlin("android")
}

if (extensions.findByType(BaseAppModuleExtension::class.java) != null) {
    configure<BaseAppModuleExtension> {
        compileSdk = 31

        defaultConfig {
            minSdk = 21
            targetSdk = 31
        }

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
    }
}
