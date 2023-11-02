package com.gnoemes.shimori.base.entities

data class ApplicationInfo(
    val packageName: String,
    val platform: Platform,
    val flavor: Flavor,
    val debug: Boolean,
    val versionName: String,
    val versionCode: Int,
    val defaultLocale: String,
)

enum class Flavor {
    Complete,
}

enum class Platform {
    Android, Jvm, Ios
}
