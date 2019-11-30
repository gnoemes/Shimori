package com.gnoemes.shimori.data.database

import androidx.room.TypeConverter
import com.gnoemes.shimori.model.anime.AnimeType
import com.gnoemes.shimori.model.common.AgeRating
import com.gnoemes.shimori.model.common.ContentStatus
import com.gnoemes.shimori.model.common.Genre
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.rate.RateTargetType
import org.joda.time.DateTime

object ShimoriTypeConverters {

    private val ageRatings by lazy(LazyThreadSafetyMode.NONE) { AgeRating.values() }
    private val contentStatuses by lazy(LazyThreadSafetyMode.NONE) { ContentStatus.values() }
    private val animeTypes by lazy(LazyThreadSafetyMode.NONE) { AnimeType.values() }
    private val rateStatuses by lazy(LazyThreadSafetyMode.NONE) { RateStatus.values() }
    private val rateTargetTypes by lazy(LazyThreadSafetyMode.NONE) { RateTargetType.values() }

    @TypeConverter
    @JvmStatic
    fun toDateTime(mills: Long?) = mills?.let { DateTime(it) }

    @TypeConverter
    @JvmStatic
    fun fromDateTime(dateTime: DateTime?): Long? = dateTime?.millis

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

}