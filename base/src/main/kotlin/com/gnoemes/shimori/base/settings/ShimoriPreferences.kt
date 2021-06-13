package com.gnoemes.shimori.base.settings

interface ShimoriPreferences {
    fun setup()

    val themePreference: Theme

    var isRussianNaming : Boolean

    enum class Theme {
        LIGHT,
        DARK,
        BATTERY_SAVER_ONLY,
        SYSTEM
    }
}