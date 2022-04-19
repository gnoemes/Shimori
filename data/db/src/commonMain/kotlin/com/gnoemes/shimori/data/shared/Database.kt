package com.gnoemes.shimori.data.shared

import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.data.base.database.ShimoriDatabase
import com.gnoemes.shimori.data.base.database.daos.RateDao
import com.gnoemes.shimori.data.db.ShimoriDB
import com.gnoemes.shimori.data.shared.daos.RateDaoImpl
import com.squareup.sqldelight.db.SqlDriver
import org.kodein.di.DI

expect val databaseModule: DI.Module

internal expect class DriverFactory {
    fun createDriver(): SqlDriver
}

internal class ShimoriSQLDelightDatabase(
    private val db: ShimoriDB,
    private val logger: Logger,
) : ShimoriDatabase {
    override val rateDao: RateDao get() = RateDaoImpl(db, logger)

}

internal fun createDatabase(driverFactory: DriverFactory, logger: Logger): ShimoriDatabase {
    val driver = driverFactory.createDriver()
    val db = ShimoriDB.invoke(
        driver = driver,
        rateAdapter = RateAdapter,
    )
    return ShimoriSQLDelightDatabase(db, logger)
}
