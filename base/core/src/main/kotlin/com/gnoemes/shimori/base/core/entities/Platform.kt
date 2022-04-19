package com.gnoemes.shimori.base.core.entities

data class Platform(
    val type : Type,
    val debug : Boolean,
    val appVersion: String = "",
    val shikimoriURL: String = "",
    val shikimoriClientId: String = "",
    val shikimoriSecretKey: String = "",
    val shikimoriRedirect: String = "",
    val shikimoriUserAgent: String = ""
) {
    enum class Type {
        Android, Jvm, Ios
    }
}