package com.gnoemes.shimori.base.settings

import kotlin.jvm.JvmInline

@JvmInline
value class AppAccentColor private constructor(val value: Int) {
    companion object {
        val System = AppAccentColor(0)
        val Red = AppAccentColor(1)
        val Orange = AppAccentColor(2)
        val Yellow = AppAccentColor(3)
        val Green = AppAccentColor(4)
        val Blue = AppAccentColor(5)
        val Purple = AppAccentColor(6)

        fun from(value: Int): AppAccentColor {
            return when (value) {
                0 -> System
                1 -> Red
                2 -> Orange
                4 -> Green
                5 -> Blue
                6 -> Purple
                else -> Yellow
            }
        }
    }
}