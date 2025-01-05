package com.gnoemes.shimori.app.core.inject

import android.app.Application
import android.content.pm.ApplicationInfo.FLAG_DEBUGGABLE
import com.gnoemes.shimori.base.entities.ApplicationInfo
import com.gnoemes.shimori.base.entities.Flavor
import com.gnoemes.shimori.base.entities.Platform
import me.tatarka.inject.annotations.Provides
import java.util.Locale

actual interface SharedPlatformApplicationComponent {
    @Provides
    fun provideApplicationInfo(
        application: Application,
        flavor: Flavor,
    ): ApplicationInfo {
        val packageManager = application.packageManager
        val applicationInfo = packageManager.getApplicationInfo(application.packageName, 0)
        val packageInfo = packageManager.getPackageInfo(application.packageName, 0)

        return ApplicationInfo(
            name = "Shimori",
            packageName = application.packageName,
            debug = (applicationInfo.flags and FLAG_DEBUGGABLE) != 0,
            flavor = flavor,
            defaultLocale = Locale.getDefault().language,
            versionName = packageInfo.versionName ?: "",
            versionCode = @Suppress("DEPRECATION") packageInfo.versionCode,
            platform = Platform.Android,
            cachePath = { application.cacheDir.absolutePath },
        )
    }
}