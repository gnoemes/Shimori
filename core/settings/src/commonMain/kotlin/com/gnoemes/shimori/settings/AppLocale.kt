package com.gnoemes.shimori.settings

@JvmInline
value class AppLocale private constructor(val value: Int) {
    companion object {
        val Russian = AppLocale(0)
        val English = AppLocale(1)

        fun from(language: String) = when (language.lowercase()) {
            "ru" -> Russian
            else -> English
        }

        fun from(locale: Int) = when (locale) {
            0 -> Russian
            else -> English
        }

        fun AppLocale.toTag(): String = when (this) {
            Russian -> "ru"
            else -> "en"
        }

    }
}