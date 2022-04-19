package com.gnoemes.shimori.data.shared

import com.gnoemes.shimori.data.db.ShimoriDB
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver

internal actual class DriverFactory {
    actual fun createDriver(): SqlDriver {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        ShimoriDB.Schema.create(driver)
        return driver
    }
}