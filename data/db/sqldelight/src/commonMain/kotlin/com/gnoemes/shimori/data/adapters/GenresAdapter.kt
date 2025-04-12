package com.gnoemes.shimori.data.adapters

import app.cash.sqldelight.ColumnAdapter

internal object GenreIdsAdapter : ColumnAdapter<List<Long>, String> {
    private const val DELIMITER = ","
    override fun decode(databaseValue: String): List<Long> =
        databaseValue.split(DELIMITER).map { it.toLong() }

    override fun encode(value: List<Long>): String =
        value.joinToString(separator = DELIMITER) { it.toString() }
}