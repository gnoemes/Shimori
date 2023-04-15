package com.gnoemes.shimori.base.core.entities

data class Platform(
    val type: Type,
    val debug: Boolean,
    val appVersion: String = "",
) {
    enum class Type {
        Android, Jvm, Ios
    }
}