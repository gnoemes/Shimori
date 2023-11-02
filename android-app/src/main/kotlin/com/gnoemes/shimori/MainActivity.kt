package com.gnoemes.shimori

import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat

class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        val component =
            AndroidActivityComponent.create(this, AndroidApplicationComponent.from(this))

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {

        }
    }
}

private fun AndroidApplicationComponent.Companion.from(context: Context): AndroidApplicationComponent {
    return (context.applicationContext as ShimoriApplication).component
}