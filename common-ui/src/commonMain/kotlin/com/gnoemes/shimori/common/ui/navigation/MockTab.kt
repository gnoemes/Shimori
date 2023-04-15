package com.gnoemes.shimori.common.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.navigator.tab.TabOptions

object MockTab : Tab() {
    @Composable
    override fun Content() {
        Box(modifier = Modifier.fillMaxSize()) {
            Text("Mock tab", modifier = Modifier.align(Alignment.Center))
        }
    }

    override val options: TabOptions
        @Composable
        get() = TabOptions(
            1u,
            "Mock",
            null
        )
}