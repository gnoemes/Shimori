package com.gnoemes.shimori.data.shared

import com.gnoemes.shimori.base.core.extensions.new
import com.gnoemes.shimori.data.base.database.ShimoriDatabase
import org.kodein.di.*

actual val databaseModule: DI.Module = DI.Module("database") {
    bind { provider { new(::DriverFactory) } }

    bindEagerSingleton { new(::createDatabase) }

    bindProvider { instance<ShimoriDatabase>().rateDao }
    bindProvider { instance<ShimoriDatabase>().rateSortDao }
    bindProvider { instance<ShimoriDatabase>().userDao }
    bindProvider { instance<ShimoriDatabase>().lastRequestDao }
    bindProvider { instance<ShimoriDatabase>().animeDao }
    bindProvider { instance<ShimoriDatabase>().mangaDao }
    bindProvider { instance<ShimoriDatabase>().ranobeDao }
}