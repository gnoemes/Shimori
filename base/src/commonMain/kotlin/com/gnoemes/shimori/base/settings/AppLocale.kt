package com.gnoemes.shimori.base.settings

import kotlin.jvm.JvmInline

@JvmInline
value class AppLocale private constructor(val value: Int) {
    companion object {
        val Russian = AppLocale(0)
        val English = AppLocale(1)

        fun from(language: String) = when (language.lowercase()) {
            "ru" -> Russian
            else -> English
        }

        fun from(locale : Int) = when(locale) {
            0 -> Russian
            else -> English
        }
    }
}