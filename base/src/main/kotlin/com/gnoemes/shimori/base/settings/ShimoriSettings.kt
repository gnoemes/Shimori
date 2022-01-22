package com.gnoemes.shimori.base.settings

interface ShimoriSettings {

    val titlesLocale: Setting<AppTitlesLocale>
    val locale: Setting<AppLocale>
    val showSpoilers : Setting<Boolean>
    val theme: Setting<AppTheme>
    val accentColor: Setting<AppAccentColor>


    val preferredListType: Setting<Int>
    val preferredListStatus: Setting<String>
}