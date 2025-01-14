package com.gnoemes.shimori.data.adapters

import app.cash.sqldelight.ColumnAdapter
import com.gnoemes.shimori.data.common.Genre
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal object GenresAdapter : ColumnAdapter<List<Genre>, String> {

    override fun decode(databaseValue: String) = Json.decodeFromString<List<Genre>>(databaseValue)
    override fun encode(value: List<Genre>): String = Json.encodeToString(value)
}