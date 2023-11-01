package com.gnoemes.shimori.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.CookieManager
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.gnoemes.shimori.base.core.settings.ShimoriSettings
import com.gnoemes.shimori.common.ui.BaseActivity
import com.gnoemes.shimori.common.ui.LocalPreferences
import com.gnoemes.shimori.common.ui.LocalShimoriSettings
import com.gnoemes.shimori.common.ui.theme.ShimoriTheme
import com.gnoemes.shimori.common.ui.utils.shouldUseDarkColors
import com.gnoemes.shimori.shikimori.auth.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.google.accompanist.web.WebView
import com.google.accompanist.web.rememberWebViewState
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import org.kodein.di.compose.withDI
import org.kodein.di.instance

class AuthActivity : BaseActivity(), DIAware {
    override val di: DI by closestDI()

    private val settings: ShimoriSettings by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            withDI(di = di) {
                val prefs =
                    LocalContext.current.getSharedPreferences("defaults", Context.MODE_PRIVATE)

                CompositionLocalProvider(
                    LocalShimoriSettings provides settings,
                    LocalPreferences provides prefs,
                ) {
                    val useDarkColors = settings.shouldUseDarkColors()

                    ShimoriTheme(useDarkColors = useDarkColors) {
                        val systemUiController = rememberSystemUiController()
                        val isLightTheme = !useDarkColors
                        val navigationColor = MaterialTheme.colorScheme.background
                        val statusBarColor = MaterialTheme.colorScheme.background

                        SideEffect {
                            systemUiController.setStatusBarColor(
                                color = statusBarColor,
                                darkIcons = isLightTheme
                            )
                            systemUiController.setNavigationBarColor(
                                color = navigationColor,
                                darkIcons = isLightTheme
                            )
                        }

                        val authIntent = intent
                        if (authIntent != null && authIntent.hasExtra(KEY_START_URL)) {
                            val startUrl = authIntent.getStringExtra(KEY_START_URL)
                            val triggerUrl = authIntent.getStringExtra(KEY_TRIGGER_URL)
                            val targetUrl = authIntent.getStringExtra(KEY_TARGET_URL)
                            val codePattern = authIntent.getStringExtra(KEY_AUTH_CODE_PATTERN)

                            if (startUrl == null || codePattern == null) {
                                finishAuth()
                                return@ShimoriTheme
                            }

                            OAuth(
                                startUrl,
                                triggerUrl,
                                targetUrl,
                                codePattern,
                            )
                        } else {
                            finishAuth()
                        }
                    }
                }
            }
        }
    }

    private fun finishAuth(data: Intent? = null) {
        val result = if (data == null) Activity.RESULT_CANCELED else Activity.RESULT_OK
        setResult(result, data)
        finish()
    }

    @Composable
    private fun OAuth(
        startUrl: String,
        triggerUrl: String?,
        targetUrl: String?,
        codePattern: String,
    ) {
        val state = rememberWebViewState(url = startUrl)
        val client = remember {
            OAuthWebViewClient(
                triggerUrl,
                targetUrl,
                codePattern.toPattern(),
            ) { code, error ->
                finishAuth(
                    Intent()
                        .putExtra(KEY_AUTH_CODE, code)
                        .putExtra(KEY_AUTH_ERROR, error)
                )
            }
        }

        WebView(
            state = state,
            captureBackPresses = false,
            onCreated = {
                with(it.settings) {
                    javaScriptEnabled = true
                }
            },
            client = client,
            onDispose = {
                        //TODO move to use case and use only after logout
//                with(CookieManager.getInstance()) {
//                    removeAllCookies(null)
//                    removeSessionCookies(null)
//                    flush()
//                }
//                it.clearCache(true)
//                it.clearHistory()
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}