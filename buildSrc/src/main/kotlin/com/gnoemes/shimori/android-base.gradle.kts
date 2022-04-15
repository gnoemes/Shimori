import com.android.build.gradle.internal.dsl.BaseAppModuleExtension

apply(plugin = "kotlin-android")

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