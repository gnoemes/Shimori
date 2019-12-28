package com.gnoemes.shimori.base.settings

interface ShimoriPreferences {
    fun setup()

    val themePreference: Theme

    enum class Theme {
        LIGHT,
        DARK,
        BATTERY_SAVER_ONLY,
        SYSTEM
    }
}