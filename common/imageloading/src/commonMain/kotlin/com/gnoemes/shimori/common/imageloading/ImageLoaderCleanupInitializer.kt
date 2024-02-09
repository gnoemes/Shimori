package com.gnoemes.shimori.common.imageloading

import com.gnoemes.shimori.base.appinitializers.AppInitializer
import com.gnoemes.shimori.base.entities.ApplicationInfo
import com.gnoemes.shimori.base.inject.ApplicationCoroutineScope
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import kotlinx.coroutines.launch
import me.tatarka.inject.annotations.Inject
import okio.FileSystem
import okio.Path.Companion.toPath

@Inject
class ImageLoaderCleanupInitializer(
    private val scope: ApplicationCoroutineScope,
    private val dispatchers: AppCoroutineDispatchers,
    private val applicationInfo: ApplicationInfo,
    private val fileSystem: Lazy<FileSystem>
) : AppInitializer {

    override fun init() {
        scope.launch(dispatchers.io) {
            val cachePath = applicationInfo.cachePath().toPath()
            val fs = fileSystem.value

            for (folder in FOLDERS) {
                val path = cachePath.resolve(folder)
                if (fs.exists(path)) {
                    fs.deleteRecursively(path)
                }
            }
        }
    }
}

private val FOLDERS = listOf("image_cache", "image_loader_cache")