package com.gnoemes.shimori.data.shared

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
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
    dispatchers: AppCoroutineDispatchers,
) : ShimoriDatabase {
    override val trackDao: TrackDao = TrackDaoImpl(db, logger, dispatchers)
    override val listSortDao: ListSortDao = ListSortDaoImpl(db, logger, dispatchers)
    override val userDao: UserDao = UserDaoImpl(db, logger, dispatchers)
    override val lastRequestDao: LastRequestDao = LastRequestDaoImpl(db, logger)
    override val animeDao: AnimeDao = AnimeDaoImpl(db, logger, dispatchers)
    override val animeVideoDao: AnimeVideoDao = AnimeVideoDaoImpl(db, logger, dispatchers)
    override val animeScreenshotDao: AnimeScreenshotDao =
        AnimeScreenshotDaoImpl(db, logger, dispatchers)
    override val mangaDao: MangaDao = MangaDaoImpl(db, logger, dispatchers)
    override val ranobeDao: RanobeDao = RanobeDaoImpl(db, logger, dispatchers)
    override val listPinDao: ListPinDao = ListPinDaoImpl(db, logger, dispatchers)
    override val trackToSyncDao: TrackToSyncDao = TrackToSyncDaoImpl(db, logger, dispatchers)
    override val characterDao: CharacterDao = CharacterDaoImpl(db, logger, dispatchers)
    override val sourceIdsSyncDao: SourceIdsSyncDao = SourceIdsSyncDaoImpl(db, logger, dispatchers)
}

internal fun createDatabase(
    driverFactory: DriverFactory,
    logger: Logger,
    dispatchers: AppCoroutineDispatchers
): ShimoriDatabase {
    val driver = driverFactory.createDriver()
    val db = ShimoriDB.invoke(
        driver = driver,
        trackAdapter = TrackAdapter,
        userAdapter = UserAdapter,
        list_sortAdapter = ListSortAdapter,
        last_requestAdapter = LastRequestAdapter,
        animeAdapter = AnimeAdapter,
        mangaAdapter = MangaAdapter,
        ranobeAdapter = RanobeAdapter,
        pinnedAdapter = PinnedAdapter,
        track_to_syncAdapter = TrackToSyncAdapter,
        character_roleAdapter = CharacterRoleAdapter,
    )
    return ShimoriSQLDelightDatabase(db, logger, dispatchers)
}
