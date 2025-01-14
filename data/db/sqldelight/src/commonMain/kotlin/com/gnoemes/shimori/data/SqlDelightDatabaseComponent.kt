package com.gnoemes.shimori.data

import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

expect interface SqlDelightDatabasePlatformComponent

@ContributesTo(AppScope::class)
interface SqlDelightDatabaseComponent : SqlDelightDatabasePlatformComponent {

    @SingleIn(AppScope::class)
    @Provides
    fun provideSqlDelightDatabase(
        factory: DatabaseFactory,
    ): ShimoriDB = factory.build()

    @SingleIn(AppScope::class)
    @Provides
    fun provideDatabaseConf(): DatabaseConfiguration = DatabaseConfiguration(inMemory = false)
}