import com.gnoemes.shimori.convention.APP_PACKAGE
import com.gnoemes.shimori.convention.readVersion
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    id("com.gnoemes.shimori.kotlin.multiplatform")
    id("com.gnoemes.shimori.compose")
    alias(libs.plugins.buildConfig)
}

val version = readVersion("${project.rootProject.projectDir}/version.properties")

val versionMajor = version["major"].toString().toInt()
val versionMinor = version["minor"].toString().toInt()
val versionPatch = version["patch"].toString().toInt()

val versionCode = version["VERSION_CODE"].toString().toInt()

val appVersionName = "$versionMajor.$versionMinor.$versionPatch"
val appPackageName = APP_PACKAGE

buildConfig {
    packageName(appPackageName)
    useKotlinOutput()

    buildConfigField("PackageName", appPackageName)
    buildConfigField("VersionName", appVersionName)
    buildConfigField("VersionCode", versionCode)
}


kotlin {
    sourceSets {
        jvmMain {
            dependencies {
                implementation(projects.app.complete)
                implementation(compose.desktop.currentOs)
                implementation(kotlinx.coroutines.swing)
                implementation(libs.slf4j)
                implementation(libs.slf4jImpl)
            }
        }
    }
}


compose.desktop {
    application {
        mainClass = "${appPackageName}.MainKt"

        nativeDistributions {
            modules("java.sql")
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = appPackageName
            //TODO: change after release or wait for fix
            //https://github.com/JetBrains/compose-multiplatform/issues/2360
//      packageVersion = appVersionName
            //for mac major version should be > 0
            packageVersion = "1.$versionMinor.$versionPatch"
        }
    }
}