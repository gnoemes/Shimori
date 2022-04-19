package com.gnoemes.shimori.data.shared

import com.gnoemes.shimori.base.core.extensions.new
import com.gnoemes.shimori.data.base.database.ShimoriDatabase
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.bindEagerSingleton
import org.kodein.di.provider

actual val databaseModule: DI.Module = DI.Module("database") {
    bind { provider { new(::DriverFactory) } }

    bindEagerSingleton { new(::createDatabase) }
}