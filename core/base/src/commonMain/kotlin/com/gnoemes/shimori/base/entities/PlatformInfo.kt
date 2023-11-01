package com.gnoemes.shimori.base.entities

data class PlatformInfo(
    val packageName: String,
    val type: Type,
    val debug: Boolean,
    val versionName: String,
    val versionCode: Int,
    val defaultLocale: String,
) {
    enum class Type {
        Android, Jvm, Ios
    }
}