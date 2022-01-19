package com.gnoemes.shimori.common.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.gnoemes.shimori.base.extensions.findWindow
import com.google.accompanist.systemuicontroller.rememberSystemUiController

//TODO CHANGE COLOR WITH BOTTOM SHEET PROGRESS
@Composable
fun NavigationBarColorSwitcher(
    newColor: Color,
    isLightTheme: Boolean = !isSystemInDarkTheme()
) {
    val systemUiController = rememberSystemUiController()

    val prevNavigationColor = LocalContext.current
        .findWindow()
        ?.navigationBarColor?.let { Color(it) } ?: return


    if (newColor != prevNavigationColor) {
        DisposableEffect(systemUiController) {
            systemUiController.setNavigationBarColor(
                    color = newColor,
                    darkIcons = isLightTheme
            )

            onDispose {
                systemUiController.setNavigationBarColor(
                        color = prevNavigationColor,
                        darkIcons = isLightTheme
                )
            }
        }
    }
}