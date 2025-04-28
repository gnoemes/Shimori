package com.gnoemes.shimori.data

import android.app.Application
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.gnoemes.shimori.ShimoriDB
import com.gnoemes.shimori.logging.api.Logger
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

actual interface SqlDelightDatabasePlatformComponent {
    @Provides
    @SingleIn(AppScope::class)
    fun provideDriverFactory(
        application: Application,
        configuration: DatabaseConfiguration,
        logger: Logger,
    ): SqlDriver = AndroidSqliteDriver(
        schema = ShimoriDB.Schema,
        context = application,
        name = when {
            configuration.inMemory -> null
            else -> "shimori.db"
        },
        callback = object : AndroidSqliteDriver.Callback(ShimoriDB.Schema) {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                setPragma(db, "foreign_keys = ON")
                setPragma(db, "JOURNAL_MODE = WAL")
                setPragma(db, "synchronous = NORMAL")
            }

            private fun setPragma(db: SupportSQLiteDatabase, pragma: String) {
                db.query("PRAGMA $pragma").use { it.moveToFirst() }
            }
        },
    ).also { driver ->
        val oldVersion = driver.executeQuery(
            identifier = null,
            sql = "PRAGMA user_version",
            parameters = 0,
            mapper = { cursor ->
                if (cursor.next().value) {
                    QueryResult.Value(cursor.getLong(0)!!)
                } else {
                    QueryResult.Value(0L)
                }
            }
        ).value

        if (oldVersion != ShimoriDB.Schema.version) {
            logger.d { "Migrating database from $oldVersion to ${ShimoriDB.Schema.version}" }

            ShimoriDB.Schema.migrate(
                driver = driver,
                oldVersion = oldVersion,
                newVersion = ShimoriDB.Schema.version
            )
        }
    }
}