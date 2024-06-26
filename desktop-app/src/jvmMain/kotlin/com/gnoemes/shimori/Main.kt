package com.gnoemes.shimori

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.gnoemes.shimori.common.ui.resources.strings.app_name
import com.gnoemes.shimori.common.ui.resources.util.Strings
import com.gnoemes.shimori.screens.ListsScreen
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.rememberCircuitNavigator
import org.jetbrains.compose.resources.stringResource

fun main() = application {
    initEnvProperties()

    val applicationComponent = remember {
        DesktopApplicationComponent.create()
    }

    LaunchedEffect(applicationComponent) {
        applicationComponent.initializers.init()
    }

    Window(
        title = stringResource(Strings.app_name),
        onCloseRequest = ::exitApplication,
    ) {
        val component = remember(applicationComponent) {
            WindowComponent.create(applicationComponent)
        }

        val backstack = rememberSaveableBackStack(listOf(ListsScreen))
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

private fun initEnvProperties() {
    System.setProperty("ShimoriPackageName", BuildConfig.PackageName)
    System.setProperty("ShimoriVersionName", BuildConfig.VersionName)
    System.setProperty("ShimoriVersionCode", BuildConfig.VersionCode.toString())
}
