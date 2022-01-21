package com.gnoemes.shimori.base.settings

interface ShimoriSettings {

    val theme: Setting<Theme>
    val dynamicColors: Setting<Boolean>
    val secondaryColor: Setting<String>
    val locale: Setting<String>

    val isRomadziNaming: Setting<Boolean>

    val preferredListType: Setting<Int>
    val preferredListStatus: Setting<String>


    enum class Theme {
        LIGHT,
        DARK,
        SYSTEM
    }
}