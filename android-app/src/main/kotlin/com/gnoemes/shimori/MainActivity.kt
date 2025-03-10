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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.repeatOnLifecycle
import com.gnoemes.shimori.common.compose.LocalActivity
import com.gnoemes.shimori.screens.HomeScreen
import com.gnoemes.shimori.settings.AppTheme
import com.slack.circuit.backstack.rememberSaveableBackStack
import com.slack.circuit.foundation.rememberCircuitNavigator
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.publicvalue.multiplatform.oidc.appsupport.AndroidCodeAuthFlowFactory

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        val applicationComponent = AndroidApplicationComponent.from(this)
        val component = applicationComponent.activityComponentFactory.createUiComponent(this)


        (applicationComponent.codeAuthFlowFactory as AndroidCodeAuthFlowFactory).registerActivity(this)

        lifecycle.coroutineScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val theme = withContext(applicationComponent.dispatchers.io) {
                    applicationComponent.settings.theme
                }
                enableEdgeToEdgeForTheme(theme.get())
                theme.observe
                    //skip initial
                    .drop(1)
                    .collect(::enableEdgeToEdgeForTheme)
            }
        }

        setContent {
            val backstack = rememberSaveableBackStack(listOf(HomeScreen))
            val navigator = rememberCircuitNavigator(backstack)

            CompositionLocalProvider(
                LocalActivity provides this
            ) {
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