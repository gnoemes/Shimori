package com.gnoemes.shimori.data.shared

import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.data.core.database.ShimoriDatabase
import com.gnoemes.shimori.data.core.database.daos.*
import com.gnoemes.shimori.data.db.ShimoriDB
import com.gnoemes.shimori.data.shared.daos.*
import com.squareup.sqldelight.db.SqlDriver
import org.kodein.di.DI

expect val databaseModule: DI.Module

internal expect class DriverFactory {
    fun createDriver(): SqlDriver
}

internal class ShimoriSQLDelightDatabase(
    db: ShimoriDB,
    logger: Logger,
) : ShimoriDatabase {
    override val rateDao: RateDao = RateDaoImpl(db, logger)
    override val rateSortDao: RateSortDao = RateSortDaoImpl(db, logger)
    override val userDao: UserDao = UserDaoImpl(db, logger)
    override val lastRequestDao: LastRequestDao = LastRequestDaoImpl(db, logger)
    override val animeDao: AnimeDao = AnimeDaoImpl(db, logger)
    override val mangaDao: MangaDao = MangaDaoImpl(db, logger)
    override val ranobeDao: RanobeDao = RanobeDaoImpl(db, logger)
    override val listPinDao: ListPinDao = ListPinDaoImpl(db, logger)
}

internal fun createDatabase(driverFactory: DriverFactory, logger: Logger): ShimoriDatabase {
    val driver = driverFactory.createDriver()
    val db = ShimoriDB.invoke(
        driver = driver,
        rateAdapter = RateAdapter,
        userAdapter = UserAdapter,
        rate_sortAdapter = RateSortAdapter,
        last_requestAdapter = LastRequestAdapter,
        animeAdapter = AnimeAdapter,
        mangaAdapter = MangaAdapter,
        ranobeAdapter = RanobeAdapter,
        pinnedAdapter = PinnedAdapter
    )
    return ShimoriSQLDelightDatabase(db, logger)
}
