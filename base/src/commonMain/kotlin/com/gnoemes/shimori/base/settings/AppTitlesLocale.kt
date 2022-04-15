package com.gnoemes.shimori.base.settings

import kotlin.jvm.JvmInline

@JvmInline
value class AppTitlesLocale private constructor(val value: Int) {
    companion object {
        val Russian = AppTitlesLocale(0)
        val English = AppTitlesLocale(1)
        val Romaji = AppTitlesLocale(2)

        fun from(locale : Int) = when(locale) {
            0 -> Russian
            1 -> English
            else -> Romaji
        }
    }
}