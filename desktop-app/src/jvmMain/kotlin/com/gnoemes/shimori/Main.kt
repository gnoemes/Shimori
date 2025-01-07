package com.gnoemes.shimori

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.gnoemes.shimori.common.ui.resources.Icons
import com.gnoemes.shimori.common.ui.resources.icons.ic_shimori
import com.gnoemes.shimori.common.ui.resources.strings.app_name
import com.gnoemes.shimori.common.ui.resources.util.Strings
import com.gnoemes.shimori.screens.HomeScreen
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.rememberCircuitNavigator
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import java.awt.Dimension

fun main() {
    val os = System.getProperty("os.name")
    when {
        os.contains("win") || os.contains("nix") || os.contains("nux") || os.contains("aix") -> System.setProperty(
            "skiko.renderApi",
            "OPENGL"
        )
        else -> System.setProperty("skiko.renderApi", "METAL")
    }
//    System.setProperty("compose.swing.render.on.graphics", "true")
    application {

        initEnvProperties()

        val applicationComponent = remember {
            DesktopApplicationComponent::class.create()
        }

        LaunchedEffect(applicationComponent) {
            applicationComponent.initializers.forEach { it.init() }
        }

        val windowState = rememberWindowState()

        Window(
            state = windowState,
            title = stringResource(Strings.app_name),
            onCloseRequest = ::exitApplication,
            icon = painterResource(Icons.ic_shimori),
        ) {
            with(LocalDensity.current) {
                window.minimumSize = Dimension(360.dp.roundToPx(), 780.dp.roundToPx())
            }

            val component = remember(applicationComponent) {
                applicationComponent.windowComponentFactory.createUiComponent()
            }

            val backstack = rememberSaveableBackStack(listOf(HomeScreen))
            val navigator = rememberCircuitNavigator(backstack) { /* no-op */ }

            component.shimoriContent.Content(
                backstack = backstack,
                navigator = navigator,
                onOpenUrl = {
                    // no-op for now
                    false
                },
                modifier = Modifier,
            )
        }
    }
}

private fun initEnvProperties() {
    System.setProperty("ShimoriPackageName", BuildConfig.PackageName)
    System.setProperty("ShimoriVersionName", BuildConfig.VersionName)
    System.setProperty("ShimoriVersionCode", BuildConfig.VersionCode.toString())
}
