package com.gnoemes.shimori.data

import android.app.Application
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import me.tatarka.inject.annotations.Provides
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

actual interface SqlDelightDatabasePlatformComponent {
    @Provides
    @SingleIn(AppScope::class)
    fun provideDriverFactory(
        application: Application,
        configuration: DatabaseConfiguration,
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
                val cursor = db.query("PRAGMA $pragma")
                cursor.moveToFirst()
                cursor.close()
            }
        },
    )
}