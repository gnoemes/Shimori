package com.gnoemes.shimori.`data`.db

import com.gnoemes.shimori.`data`.base.entities.rate.RateStatus
import com.gnoemes.shimori.`data`.base.entities.rate.RateTargetType
import com.squareup.sqldelight.ColumnAdapter
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlinx.datetime.DateTimePeriod

public data class Rate(
  public val id: Long,
  public val shikimori_id: Long?,
  public val target_id: Long,
  public val target_type: RateTargetType,
  public val status: RateStatus,
  public val score: Int?,
  public val comment: String?,
  public val progress: Int,
  public val re_counter: Int,
  public val date_created: DateTimePeriod?,
  public val date_updated: DateTimePeriod?
) {
  public override fun toString(): String = """
  |Rate [
  |  id: $id
  |  shikimori_id: $shikimori_id
  |  target_id: $target_id
  |  target_type: $target_type
  |  status: $status
  |  score: $score
  |  comment: $comment
  |  progress: $progress
  |  re_counter: $re_counter
  |  date_created: $date_created
  |  date_updated: $date_updated
  |]
  """.trimMargin()

  public class Adapter(
    public val target_typeAdapter: ColumnAdapter<RateTargetType, String>,
    public val statusAdapter: ColumnAdapter<RateStatus, String>,
    public val date_createdAdapter: ColumnAdapter<DateTimePeriod, String>,
    public val date_updatedAdapter: ColumnAdapter<DateTimePeriod, String>
  )
}
