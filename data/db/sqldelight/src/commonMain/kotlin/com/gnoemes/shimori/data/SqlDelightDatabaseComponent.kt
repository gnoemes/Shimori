package com.gnoemes.shimori.data

import com.gnoemes.shimori.base.inject.ApplicationScope
import com.gnoemes.shimori.data.daos.AnimeDaoImpl
import com.gnoemes.shimori.data.daos.AnimeScreenshotDaoImpl
import com.gnoemes.shimori.data.daos.AnimeVideoDaoImpl
import com.gnoemes.shimori.data.daos.CharacterDaoImpl
import com.gnoemes.shimori.data.daos.LastRequestDaoImpl
import com.gnoemes.shimori.data.daos.ListPinDaoImpl
import com.gnoemes.shimori.data.daos.ListSortDaoImpl
import com.gnoemes.shimori.data.daos.MangaDaoImpl
import com.gnoemes.shimori.data.daos.RanobeDaoImpl
import com.gnoemes.shimori.data.daos.SourceIdsSyncDaoImpl
import com.gnoemes.shimori.data.daos.TrackDaoImpl
import com.gnoemes.shimori.data.daos.TrackToSyncDaoImpl
import com.gnoemes.shimori.data.daos.UserDaoImpl
import com.gnoemes.shimori.data.db.api.daos.AnimeDao
import com.gnoemes.shimori.data.db.api.daos.AnimeScreenshotDao
import com.gnoemes.shimori.data.db.api.daos.AnimeVideoDao
import com.gnoemes.shimori.data.db.api.daos.CharacterDao
import com.gnoemes.shimori.data.db.api.daos.LastRequestDao
import com.gnoemes.shimori.data.db.api.daos.ListPinDao
import com.gnoemes.shimori.data.db.api.daos.ListSortDao
import com.gnoemes.shimori.data.db.api.daos.MangaDao
import com.gnoemes.shimori.data.db.api.daos.RanobeDao
import com.gnoemes.shimori.data.db.api.daos.SourceIdsSyncDao
import com.gnoemes.shimori.data.db.api.daos.TrackDao
import com.gnoemes.shimori.data.db.api.daos.TrackToSyncDao
import com.gnoemes.shimori.data.db.api.daos.UserDao
import com.gnoemes.shimori.data.db.api.db.DatabaseTransactionRunner
import me.tatarka.inject.annotations.Provides

expect interface SqlDelightDatabasePlatformComponent

interface SqlDelightDatabaseComponent : SqlDelightDatabasePlatformComponent {

    @ApplicationScope
    @Provides
    fun provideSqlDelightDatabase(
        factory: DatabaseFactory,
    ): ShimoriDB = factory.build()

    @ApplicationScope
    @Provides
    fun provideDatabaseConf(): DatabaseConfiguration = DatabaseConfiguration(inMemory = false)

    @ApplicationScope
    @Provides
    fun provideDatabaseTransactionRunner(runner: SqlDelightTransactionRunner): DatabaseTransactionRunner =
        runner

    @ApplicationScope
    @Provides
    fun bindAnimeDao(dao: AnimeDaoImpl): AnimeDao = dao

    @ApplicationScope
    @Provides
    fun bindAnimeScreenshotDao(dao: AnimeScreenshotDaoImpl): AnimeScreenshotDao = dao

    @ApplicationScope
    @Provides
    fun bindAnimeVideoDao(dao: AnimeVideoDaoImpl): AnimeVideoDao = dao

    @ApplicationScope
    @Provides
    fun bindCharacterDao(dao: CharacterDaoImpl): CharacterDao = dao

    @ApplicationScope
    @Provides
    fun bindLastRequestDao(dao: LastRequestDaoImpl): LastRequestDao = dao

    @ApplicationScope
    @Provides
    fun bindListPinDao(dao: ListPinDaoImpl): ListPinDao = dao

    @ApplicationScope
    @Provides
    fun bindListSortDao(dao: ListSortDaoImpl): ListSortDao = dao

    @ApplicationScope
    @Provides
    fun bindMangaDao(dao: MangaDaoImpl): MangaDao = dao

    @ApplicationScope
    @Provides
    fun bindRanobeDao(dao: RanobeDaoImpl): RanobeDao = dao

    @ApplicationScope
    @Provides
    fun bindSourceIdsSyncDao(dao: SourceIdsSyncDaoImpl): SourceIdsSyncDao = dao

    @ApplicationScope
    @Provides
    fun bindTrackDao(dao: TrackDaoImpl): TrackDao = dao

    @ApplicationScope
    @Provides
    fun bindTrackToSyncDao(dao: TrackToSyncDaoImpl): TrackToSyncDao = dao

    @ApplicationScope
    @Provides
    fun bindUserDao(dao: UserDaoImpl): UserDao = dao
}