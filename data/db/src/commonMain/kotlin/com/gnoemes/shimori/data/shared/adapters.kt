package com.gnoemes.shimori.data.shared

import com.gnoemes.shimori.data.base.entities.rate.RateStatus
import com.gnoemes.shimori.data.base.entities.rate.RateTargetType
import com.squareup.sqldelight.ColumnAdapter
import comgnoemesshimoridatadb.Rate
import kotlinx.datetime.DateTimePeriod

internal object DateTimePeriodAdapter : ColumnAdapter<DateTimePeriod, String> {
    override fun decode(databaseValue: String) = DateTimePeriod.parse(databaseValue)
    override fun encode(value: DateTimePeriod) = value.toString()
}

internal object RateTargetPeriodAdapter : ColumnAdapter<RateTargetType, String> {
    override fun decode(databaseValue: String) = RateTargetType.valueOf(databaseValue)
    override fun encode(value: RateTargetType) = value.name
}

internal object RateStatusPeriodAdapter : ColumnAdapter<RateStatus, String> {
    override fun decode(databaseValue: String) = RateStatus.valueOf(databaseValue)
    override fun encode(value: RateStatus): String = value.name
}


internal val RateAdapter = Rate.Adapter(
    target_typeAdapter = RateTargetPeriodAdapter,
    statusAdapter = RateStatusPeriodAdapter,
    date_createdAdapter = DateTimePeriodAdapter,
    date_updatedAdapter = DateTimePeriodAdapter
)