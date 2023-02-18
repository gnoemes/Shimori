package com.gnoemes.shimori.data.shared

import com.gnoemes.shimori.base.core.extensions.new
import com.gnoemes.shimori.data.core.database.ShimoriDatabase
import org.kodein.di.*

actual val databaseModule: DI.Module = DI.Module("database") {
    bind { provider { new(::DriverFactory) } }

    bindEagerSingleton { new(::createDatabase) }

    bindProvider { instance<ShimoriDatabase>().trackDao }
    bindProvider { instance<ShimoriDatabase>().listSortDao }
    bindProvider { instance<ShimoriDatabase>().userDao }
    bindProvider { instance<ShimoriDatabase>().lastRequestDao }
    bindProvider { instance<ShimoriDatabase>().animeDao }
    bindProvider { instance<ShimoriDatabase>().animeVideoDao }
    bindProvider { instance<ShimoriDatabase>().mangaDao }
    bindProvider { instance<ShimoriDatabase>().ranobeDao }
    bindProvider { instance<ShimoriDatabase>().listPinDao }
    bindProvider { instance<ShimoriDatabase>().trackToSyncDao }
    bindProvider { instance<ShimoriDatabase>().characterDao }
    bindProvider { instance<ShimoriDatabase>().sourceIdsSyncDao }
}