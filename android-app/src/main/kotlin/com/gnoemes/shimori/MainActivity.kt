package com.gnoemes.shimori

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import com.gnoemes.shimori.screens.ListsScreen
import com.gnoemes.shimori.settings.AppTheme
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.rememberCircuitNavigator
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        enableEdgeToEdgeForTheme(AppTheme.SYSTEM)

        super.onCreate(savedInstanceState)

        val applicationComponent = AndroidApplicationComponent.from(this)
        val component = AndroidActivityComponent.create(this, applicationComponent)

        lifecycle.coroutineScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val settings = withContext(applicationComponent.dispatchers.io) {
                    applicationComponent.settings.theme.observe
                }
                settings.collect(::enableEdgeToEdgeForTheme)
            }
        }

        setContent {
            val backstack = rememberSaveableBackStack(listOf(ListsScreen))
            val navigator = rememberCircuitNavigator(backstack)

            component.shimoriContent.Content(
                backstack,
                navigator,
                { url ->
                    val intent = CustomTabsIntent.Builder().build()
                    intent.launchUrl(this@MainActivity, Uri.parse(url))
                    true
                },
                Modifier
            )
        }
    }
}

private fun ComponentActivity.enableEdgeToEdgeForTheme(theme: AppTheme) {
    val style = when (theme) {
        AppTheme.LIGHT -> SystemBarStyle.light(Color.TRANSPARENT, Color.TRANSPARENT)
        AppTheme.DARK -> SystemBarStyle.dark(Color.TRANSPARENT)
        AppTheme.SYSTEM -> SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
    }
    enableEdgeToEdge(statusBarStyle = style, navigationBarStyle = style)
}

private fun AndroidApplicationComponent.Companion.from(context: Context): AndroidApplicationComponent {
    return (context.applicationContext as ShimoriApplication).component
}