package com.gnoemes.shimori.data.database

import android.content.Context
import androidx.room.Room
import com.gnoemes.shimori.data.daos.EntityInserter
import com.gnoemes.shimori.data.util.DatabaseTransactionRunner
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class RoomDatabaseModule {
    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): ShimoriRoomDatabase =
        Room.databaseBuilder(context, ShimoriRoomDatabase::class.java, "shimori.db")
            .fallbackToDestructiveMigration()
            .build()
}

@InstallIn(SingletonComponent::class)
@Module
abstract class DatabaseBindings {

    @Binds
    abstract fun bindDatabase(db: ShimoriRoomDatabase): ShimoriDatabase

    @Binds
    abstract fun bindTransactionRunner(runner: RoomTransactionRunner): DatabaseTransactionRunner

    @Binds
    abstract fun bindEntityInserter(inserter: ShimoriEntityInserter): EntityInserter
}

@InstallIn(SingletonComponent::class)
@Module
class DaoModule {
    @Provides
    fun provideAnimeDao(db: ShimoriDatabase) = db.animeDao()

    @Provides
    fun provideRateDao(db: ShimoriDatabase) = db.rateDao()

    @Provides
    fun provideLastRequestDao(db: ShimoriDatabase) = db.lastRequestDao()

    @Provides
    fun provideUserDao(db: ShimoriDatabase) = db.userDao()

    @Provides
    fun provideRateSortDao(db: ShimoriDatabase) = db.rateSortDao()
}
