// Copyright 2023, Christopher Banes and the Tivi project contributors
// SPDX-License-Identifier: Apache-2.0

package com.gnoemes.shimori.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn
import java.io.File

actual interface SqlDelightDatabasePlatformComponent {

    @Provides
    @SingleIn(AppScope::class)
    fun provideDriverFactory(
        configuration: DatabaseConfiguration
    ): SqlDriver = JdbcSqliteDriver(
        url = when {
            configuration.inMemory -> JdbcSqliteDriver.IN_MEMORY
            else -> "jdbc:sqlite:${getDatabaseFile().absolutePath}"
        }
    ).also { db ->
        val version = db.execute(
            identifier = null,
            sql = "PRAGMA user_version;",
            parameters = 0,
        )

        if (version.value == 0L) {
//            ShimoriDB.Schema.create(db)
            db.execute(null, "PRAGMA foreign_keys=ON;", 0)
            db.execute(null, sql = "PRAGMA user_version = 1;", 0)
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

