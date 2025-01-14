import com.gnoemes.shimori.convention.ProjectConfig
import com.gnoemes.shimori.convention.readVersion
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.shimori.kotlin.multiplatform.common)
    alias(libs.plugins.shimori.compose)
    alias(libs.plugins.buildConfig)
}

val version = readVersion("${project.rootProject.projectDir}/version.properties")

val versionMajor = version["major"].toString().toInt()
val versionMinor = version["minor"].toString().toInt()
val versionPatch = version["patch"].toString().toInt()

val versionCode = version["VERSION_CODE"].toString().toInt()

val appVersionName = "$versionMajor.$versionMinor.$versionPatch"
val appPackageName = ProjectConfig.APP_PACKAGE

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
            }
        }
    }
}


compose.desktop {
    application {
        mainClass = "${appPackageName}.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            macOS {
                iconFile.set(project.file("ic_launcher.png"))
            }

            modules("java.sql")

            packageName = appPackageName
            //TODO: change after release or wait for fix
            //https://github.com/JetBrains/compose-multiplatform/issues/2360
//      packageVersion = appVersionName
            //for mac major version should be > 0
            packageVersion = "1.$versionMinor.$versionPatch"
        }
    }
}