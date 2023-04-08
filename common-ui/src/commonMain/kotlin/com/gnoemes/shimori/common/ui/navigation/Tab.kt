package com.gnoemes.shimori.common.ui.navigation

import cafe.adriel.voyager.navigator.Navigator

interface Tab : cafe.adriel.voyager.navigator.tab.Tab {
    suspend fun onReselect(navigator: Navigator) {}
}