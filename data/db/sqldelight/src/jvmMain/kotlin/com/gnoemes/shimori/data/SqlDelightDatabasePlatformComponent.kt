// Copyright 2023, Christopher Banes and the Tivi project contributors
// SPDX-License-Identifier: Apache-2.0

package com.gnoemes.shimori.data

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.gnoemes.shimori.base.inject.ApplicationScope
import me.tatarka.inject.annotations.Provides
import java.io.File

actual interface SqlDelightDatabasePlatformComponent {

    @Provides
    @ApplicationScope
    fun provideDriverFactory(
        configuration: DatabaseConfiguration
    ) : SqlDriver = JdbcSqliteDriver(
        url = when {
            configuration.inMemory -> JdbcSqliteDriver.IN_MEMORY
            else -> "jdbc:sqlite:${getDatabaseFile().absolutePath}"
        }
    ).also {db ->
        ShimoriDB.Schema.create(db)
        db.execute(null, "PRAGMA foreign_keys=ON", 0)
    }
}


private fun getDatabaseFile() : File = File(
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

