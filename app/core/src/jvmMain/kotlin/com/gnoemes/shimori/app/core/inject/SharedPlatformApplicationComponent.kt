package com.gnoemes.shimori.app.core.inject

import com.gnoemes.shimori.base.entities.ApplicationInfo
import com.gnoemes.shimori.base.entities.Flavor
import com.gnoemes.shimori.base.entities.Platform
import me.tatarka.inject.annotations.Provides
import java.io.File
import java.util.Locale

actual interface SharedPlatformApplicationComponent {
    @Provides
    fun provideApplicationInfo(
        flavor: Flavor,
    ): ApplicationInfo {

//        val isDebug = ManagementFactory.getRuntimeMXBean()
//            .inputArguments
//            .toString().indexOf("jdwp") >= 0;

        return ApplicationInfo(
            name = "Shimori",
            packageName = System.getProperty("ShimoriPackageName"),
            debug = true,
            flavor = flavor,
            defaultLocale = Locale.getDefault().language,
            versionName = System.getProperty("ShimoriVersionName"),
            versionCode = System.getProperty("ShimoriVersionCode").toInt(),
            platform = Platform.Jvm,
            cachePath = { getCacheDir().absolutePath },
        )
    }
}

private fun getCacheDir(): File = when (currentOperatingSystem) {
    OperatingSystem.Windows -> File(System.getenv("AppData"), "shimori/cache")
    OperatingSystem.Linux -> File(System.getProperty("user.home"), ".cache/shimori")
    OperatingSystem.MacOS -> File(System.getProperty("user.home"), "Library/Caches/shimori")
    else -> throw IllegalStateException("Unsupported operating system")
}

internal enum class OperatingSystem {
    Windows,
    Linux,
    MacOS,
    Unknown,
}

private val currentOperatingSystem: OperatingSystem
    get() {
        val os = System.getProperty("os.name").lowercase()
        return when {
            os.contains("win") -> OperatingSystem.Windows
            os.contains("nix") || os.contains("nux") || os.contains("aix") -> OperatingSystem.Linux
            os.contains("mac") -> OperatingSystem.MacOS
            else -> OperatingSystem.Unknown
        }
    }
