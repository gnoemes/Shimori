package com.gnoemes.shimori.base.settings

import kotlinx.coroutines.flow.Flow

interface ShimoriPreferences {
    fun setup()

    var theme: Theme
    fun observeTheme() : Flow<Theme>

    var useDynamicColors : Boolean

    var isRomadziNaming : Boolean

    var preferredListType : Int

    enum class Theme {
        LIGHT,
        DARK,
        SYSTEM
    }
}