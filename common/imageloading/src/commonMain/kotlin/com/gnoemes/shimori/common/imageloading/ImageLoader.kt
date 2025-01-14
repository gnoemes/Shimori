package com.gnoemes.shimori.common.imageloading

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.disk.DiskCache
import coil3.intercept.Interceptor
import coil3.memory.MemoryCache
import coil3.util.Logger.Level
import com.gnoemes.shimori.base.entities.ApplicationInfo
import com.gnoemes.shimori.logging.api.Logger
import okio.Path.Companion.toPath

internal fun shimoriLoader(
    context: PlatformContext,
    interceptors: Set<Interceptor>,
    applicationInfo: ApplicationInfo,
    logger: Logger,
    debug: Boolean = false,
): ImageLoader {
    return ImageLoader.Builder(context)
        .components {
            interceptors.forEach(::add)
        }
        .memoryCache {
            MemoryCache.Builder()
                .maxSizePercent(context, percent = 0.25)
                .build()
        }
        .diskCache {
            DiskCache.Builder()
                .directory(applicationInfo.cachePath().toPath().resolve("coil_cache"))
                .build()
        }
        .apply {
            if (debug) {
                logger(logger.asCoilLogger())
            }
        }
        .build()
}

private fun Logger.asCoilLogger(): coil3.util.Logger = object : coil3.util.Logger {
    override var minLevel: Level = Level.Debug

    override fun log(
        tag: String,
        level: Level,
        message: String?,
        throwable: Throwable?
    ) {
        when (level) {
            Level.Verbose -> v(throwable) { message.orEmpty() }
            Level.Debug -> d(throwable) { message.orEmpty() }
            Level.Info -> i(throwable) { message.orEmpty() }
            Level.Warn -> w(throwable) { message.orEmpty() }
            Level.Error -> e(throwable) { message.orEmpty() }
        }
    }
}