package com.gnoemes.shimori.data.adapters

import app.cash.sqldelight.ColumnAdapter
import kotlinx.datetime.LocalDate

internal object LocalDateAdapter : ColumnAdapter<LocalDate, String> {
    override fun decode(databaseValue: String) = LocalDate.parse(databaseValue)
    override fun encode(value: LocalDate) = value.toString()
}