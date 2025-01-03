import com.android.build.gradle.internal.dsl.BaseAppModuleExtension
import com.gnoemes.shimori.convention.propOrDef
import com.gnoemes.shimori.convention.readVersion

plugins {
    id("com.gnoemes.shimori.android.application")
    id("com.gnoemes.shimori.kotlin.android")
    id("com.gnoemes.shimori.compose")
}

val useReleaseKeystore = rootProject.file("release/app-release.jks").exists()

android {
    namespace = "com.gnoemes.shimori"
    val version = readVersion("${project.rootProject.projectDir}/version.properties")

    val versionMajor = version["major"].toString().toInt()
    val versionMinor = version["minor"].toString().toInt()
    val versionPatch = version["patch"].toString().toInt()

    val appVersionName = "$versionMajor.$versionMinor.$versionPatch"
    val appVersionCode = version["VERSION_CODE"].toString().toIntOrNull()

    println("config code: $appVersionCode, name: $appVersionName")

    defaultConfig {
        versionCode = appVersionCode
        versionName = appVersionName
        setProperty("archivesBaseName", "Shimori-v$appVersionName($appVersionCode)")

        addManifestPlaceholders(
            //TODO move some constants to project level
            mapOf("oidcRedirectScheme" to "shimori")
        )
    }

    buildFeatures {
        buildConfig = true
    }

    signingConfigs {
        getByName("debug") {
            storeFile = rootProject.file("release/app-debug.jks")
            storePassword = "password"
            keyAlias = "debugkey"
            keyPassword = "password"
        }

        create("release") {
            if (useReleaseKeystore) {
                storeFile = rootProject.file("release/app-release.jks")
                storePassword = propOrDef("ReleaseStorePassword", "").toString()
                keyAlias = "shimori"
                keyPassword = propOrDef("ReleaseKeyPassword", "").toString()
            }
        }
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs["debug"]
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-dev"
        }
        release {
            signingConfig = signingConfigs[if (useReleaseKeystore) "release" else "debug"]
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
        create("qa") {
            matchingFallbacks.add("release")
            isDebuggable = true
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getByName("debug")
            versionNameSuffix = "-qa"
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    flavorDimensions += "mode"
    productFlavors {
        create("complete") {
            dimension = "mode"
        }
    }

    packaging {
        resources.excludes += setOf(
            // Exclude AndroidX version files
            "META-INF/*.version",
            // Exclude consumer proguard files
            "META-INF/proguard/*",
            // Exclude the Firebase/Fabric/other random properties files
            "/*.properties",
            "fabric/*.properties",
            "META-INF/*.properties",
            // License files
            "LICENSE*",
            // Exclude Kotlin unused files
            "META-INF/**/previous-compilation-data.bin",
        )
    }
}

dependencies {
    completeImplementation(projects.app.complete)

    implementation(androidx.activity)
    implementation(androidx.browser)
    implementation(androidx.splashscreen)
    implementation(androidx.work.runtime)
    implementation(kotlinx.coroutines.android)
    implementation(composelibs.activity)
    implementation(libs.multiplatform.oidc)
}

val firebaseAppId = project.propOrDef("firebaseAppId", "").toString()
if (firebaseAppId.isNotEmpty()) {
    apply(plugin = "com.google.firebase.appdistribution")

    configure<BaseAppModuleExtension> {
        buildTypes {
            getByName("release") {
                configure<com.google.firebase.appdistribution.gradle.AppDistributionExtension> {
                    appId = firebaseAppId
                    groups = "qa"
                    artifactType = "APK"
                }
            }
        }
    }
}

if (file("google-services.json").exists()) {
    apply(plugin = "com.google.gms.google-services")
    apply(plugin = "com.google.firebase.crashlytics")
}

fun DependencyHandler.completeImplementation(dependencyNotation: Any) =
    add("completeImplementation", dependencyNotation)