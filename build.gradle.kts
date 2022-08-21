import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }

    dependencies {
        classpath(libs.kotlin.serialization.gradle)
        classpath(libs.sqldelight.gradle)
        classpath(libs.google.gmsGoogleServices)
        classpath(libs.google.crashlyticsGradle)
        classpath(libs.google.appDistribution)
        //android & kotlin gradle plugins in buildSrc/build.gradle.kts
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
    }
}

subprojects {
    tasks.withType(KotlinCompile::class.java).configureEach {
        kotlinOptions {
            freeCompilerArgs += arrayOf(
                // Enable experimental coroutines APIs, including Flow, context receivers
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-opt-in=kotlinx.coroutines.FlowPreview",
                "-opt-in=kotlin.Experimental",
                "-Xcontext-receivers",
            )

            // Set JVM target to 1.11
            jvmTarget = JavaVersion.VERSION_11.toString()
        }
    }

    configurations.configureEach {
        // We forcefully exclude AppCompat + MDC from any transitive dependencies.
        // This is a Compose app, so there's no need for these.
        exclude(group = "androidx.appcompat")
        exclude(group = "com.google.android.material", module = "material")

        resolutionStrategy.eachDependency {
            if (requested.group == "org.jetbrains.kotlinx"
                && requested.module.name == "kotlinx-collections-immutable-jvm"
            ) {
                // kotlinx-collections-immutable-jvm 0.3.4+ is available on Maven Central
                useVersion("0.3.4")
            }
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}