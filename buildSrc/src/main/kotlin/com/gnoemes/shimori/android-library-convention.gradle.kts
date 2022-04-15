plugins {
    id("base-module")
    id("com.android.library")
    kotlin("android")
    id("android-base")
}

android {
    sourceSets.all { java.srcDir("src/$name/kotlin") }
}
