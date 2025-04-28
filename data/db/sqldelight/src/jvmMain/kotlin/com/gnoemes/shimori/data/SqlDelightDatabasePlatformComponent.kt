// Copyright 2023, Christopher Banes and the Tivi project contributors
// SPDX-License-Identifier: Apache-2.0

package com.gnoemes.shimori.data

import app.cash.sqldelight.db.QueryResult
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.gnoemes.shimori.ShimoriDB
import com.gnoemes.shimori.logging.api.Logger
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import java.io.File

actual interface SqlDelightDatabasePlatformComponent {

    @Provides
    @SingleIn(AppScope::class)
    fun provideDriverFactory(
        configuration: DatabaseConfiguration,
        logger: Logger
    ): SqlDriver = JdbcSqliteDriver(
        url = when {
            configuration.inMemory -> JdbcSqliteDriver.IN_MEMORY
            else -> "jdbc:sqlite:${getDatabaseFile().absolutePath}"
        }
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

        if (oldVersion == 0L) {
            ShimoriDB.Schema.create(driver)
            driver.execute(null, "PRAGMA foreign_keys=ON;", 0)
            driver.execute(null, sql = "PRAGMA user_version = 1;", 0)
        }

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


private fun getDatabaseFile(): File = File(
    appDir.also { if (!it.exists()) it.mkdirs() },
    "shimori.db",
)

private val appDir: File
    get() {
        val os = System.getProperty("os.name").lowercase()
        return when {
            os.contains("win") -> {
                File(System.getenv("AppData"), "shimori/db")
            }

            os.contains("nix") || os.contains("nux") || os.contains("aix") -> {
                File(System.getProperty("user.home"), ".shimori")
            }

            os.contains("mac") -> {
                File(System.getProperty("user.home"), "Library/Application Support/shimori")
            }

            else -> error("Unsupported operating system")
        }
    }

