package com.gnoemes.shimori.`data`.db

import com.gnoemes.shimori.`data`.base.entities.rate.RateStatus
import com.gnoemes.shimori.`data`.base.entities.rate.RateTargetType
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlin.Any
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Unit
import kotlinx.datetime.DateTimePeriod

public interface RateQueries : Transacter {
  public fun <T : Any> queryAll(mapper: (
    id: Long,
    shikimori_id: Long?,
    target_id: Long,
    target_type: RateTargetType,
    status: RateStatus,
    score: Int?,
    comment: String?,
    progress: Int,
    re_counter: Int,
    date_created: DateTimePeriod?,
    date_updated: DateTimePeriod?
  ) -> T): Query<T>

  public fun queryAll(): Query<Rate>

  public fun <T : Any> queryById(id: Long, mapper: (
    id: Long,
    shikimori_id: Long?,
    target_id: Long,
    target_type: RateTargetType,
    status: RateStatus,
    score: Int?,
    comment: String?,
    progress: Int,
    re_counter: Int,
    date_created: DateTimePeriod?,
    date_updated: DateTimePeriod?
  ) -> T): Query<T>

  public fun queryById(id: Long): Query<Rate>

  public fun <T : Any> queryByShikimoriId(shikimori_id: Long?, mapper: (
    id: Long,
    shikimori_id: Long?,
    target_id: Long,
    target_type: RateTargetType,
    status: RateStatus,
    score: Int?,
    comment: String?,
    progress: Int,
    re_counter: Int,
    date_created: DateTimePeriod?,
    date_updated: DateTimePeriod?
  ) -> T): Query<T>

  public fun queryByShikimoriId(shikimori_id: Long?): Query<Rate>

  public fun <T : Any> queryByTarget(
    id: Long,
    type: RateTargetType,
    mapper: (
      id: Long,
      shikimori_id: Long?,
      target_id: Long,
      target_type: RateTargetType,
      status: RateStatus,
      score: Int?,
      comment: String?,
      progress: Int,
      re_counter: Int,
      date_created: DateTimePeriod?,
      date_updated: DateTimePeriod?
    ) -> T
  ): Query<T>

  public fun queryByTarget(id: Long, type: RateTargetType): Query<Rate>

  public fun queryCount(): Query<Long>

  public fun statusForTypeExist(target_type: RateTargetType, status: RateStatus): Query<Long>

  public fun insert(rate: Rate): Unit

  public fun deleteById(id: Long): Unit

  public fun deleteAll(): Unit
}
