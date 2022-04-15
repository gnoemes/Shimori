import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import org.jetbrains.kotlin.gradle.plugin.KaptExtension

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

try {
    project.extensions.getByType(KaptExtension::class.java)

    configure<KaptExtension> {
        correctErrorTypes = true
        useBuildCache = true
    }
} catch (e : UnknownDomainObjectException) {
    println("Kapt not provided")
}


