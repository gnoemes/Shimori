package com.gnoemes.shimori.data.shared

import android.content.Context
import com.gnoemes.shimori.data.db.ShimoriDB
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver

internal actual class DriverFactory(
    private val context: Context
) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = ShimoriDB.Schema,
            context = context
        )
    }
}