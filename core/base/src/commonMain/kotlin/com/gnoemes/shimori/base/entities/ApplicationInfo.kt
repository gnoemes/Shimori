package com.gnoemes.shimori.base.entities

import com.gnoemes.shimori.base.entities.Platform.Android
import com.gnoemes.shimori.base.entities.Platform.Jvm

data class ApplicationInfo(
    val name: String,
    val packageName: String,
    val platform: Platform,
    val flavor: Flavor,
    val debug: Boolean,
    val versionName: String,
    val versionCode: Int,
    val defaultLocale: String,
    val cachePath: () -> String
)

enum class Flavor {
    Complete,
}

enum class Platform {
    Android, Jvm;
}

val Platform.isAndroid: Boolean get() = this == Android
val Platform.isDesktop: Boolean get() = this == Jvm
