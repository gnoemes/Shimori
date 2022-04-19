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
        //android & kotlin gradle plugins in buildSrc/build.gradle.kts
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

subprojects {
    tasks.withType(KotlinCompile::class.java).configureEach {
        kotlinOptions {
            freeCompilerArgs += arrayOf(
                // Enable experimental coroutines APIs, including Flow
                "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-Xopt-in=kotlinx.coroutines.FlowPreview",
                "-Xopt-in=kotlin.Experimental",
            )

            // Set JVM target to 1.8
            jvmTarget = "1.8"
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