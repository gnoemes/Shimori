package com.gnoemes.shimori.data.database

import androidx.room.TypeConverter
import com.gnoemes.shimori.model.anime.AnimeType
import com.gnoemes.shimori.model.app.Request
import com.gnoemes.shimori.model.common.AgeRating
import com.gnoemes.shimori.model.common.ContentStatus
import com.gnoemes.shimori.model.common.Genre
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.rate.RateTargetType
import org.joda.time.DateTime
import org.joda.time.Instant
import org.joda.time.format.ISODateTimeFormat

object ShimoriTypeConverters {
    private val dateFormat = ISODateTimeFormat.dateTimeNoMillis()

    private val ageRatings by lazy(LazyThreadSafetyMode.NONE) { AgeRating.values() }
    private val contentStatuses by lazy(LazyThreadSafetyMode.NONE) { ContentStatus.values() }
    private val animeTypes by lazy(LazyThreadSafetyMode.NONE) { AnimeType.values() }
    private val rateStatuses by lazy(LazyThreadSafetyMode.NONE) { RateStatus.values() }
    private val rateTargetTypes by lazy(LazyThreadSafetyMode.NONE) { RateTargetType.values() }
    private val requestValues by lazy(LazyThreadSafetyMode.NONE) { Request.values() }

    @TypeConverter
    @JvmStatic
    fun toDateTime(value: String?) = value?.let { dateFormat.parseDateTime(value) }

    @TypeConverter
    @JvmStatic
    fun fromDateTime(dateTime: DateTime?): String? = dateTime?.toString(dateFormat)

    @TypeConverter
    @JvmStatic
    fun toInstant(value: Long?) = value?.let { Instant(it) }

    @TypeConverter
    @JvmStatic
    fun fromInstant(instant: Instant?) = instant?.millis

    @TypeConverter
    @JvmStatic
    fun toAnimeType(type: String?) = animeTypes.firstOrNull { it.type == type }

    @TypeConverter
    @JvmStatic
    fun fromAnimeType(type: AnimeType?) = type?.type

    @TypeConverter
    @JvmStatic
    fun toContentStatus(status: String?) = contentStatuses.firstOrNull { it.status == status }

    @TypeConverter
    @JvmStatic
    fun fromContentStatus(status: ContentStatus?) = status?.status

    @TypeConverter
    @JvmStatic
    fun toAgeRating(rating: String?) = ageRatings.firstOrNull { it.rating == rating }

    @TypeConverter
    @JvmStatic
    fun fromAgeRating(rating: AgeRating?) = rating?.rating

    @TypeConverter
    @JvmStatic
    fun toRateTargetType(type: String?) = rateTargetTypes.firstOrNull { it.type == type }

    @TypeConverter
    @JvmStatic
    fun fromRateTargetType(type: RateTargetType?) = type?.type

    @TypeConverter
    @JvmStatic
    fun toRateStatus(status: String?) = rateStatuses.firstOrNull { it.shikimoriValue == status }

    @TypeConverter
    @JvmStatic
    fun fromRateStatus(status: RateStatus?) = status?.shikimoriValue

    @TypeConverter
    @JvmStatic
    fun toGenres(genres: String?): List<Genre>? =
        genres?.split(',')?.mapNotNull { Genre.fromShikimori(it) }

    @TypeConverter
    @JvmStatic
    fun fromGenres(genres: List<Genre>?): String? {
        if (genres.isNullOrEmpty()) return null

        val builder = StringBuilder()

        genres.forEach { builder.append(it.shikimoriValue).append(',') }

        return builder.toString()
    }

    @TypeConverter
    @JvmStatic
    fun toRequest(tag: String) = requestValues.firstOrNull { it.tag == tag }

    @TypeConverter
    @JvmStatic
    fun fromRequest(request: Request) = request.tag

    @TypeConverter
    @JvmStatic
    fun toBoolean(value: Int) = value != 0

    @TypeConverter
    @JvmStatic
    fun fromBoolean(value: Boolean?) = if (value == true) 1 else 0

}