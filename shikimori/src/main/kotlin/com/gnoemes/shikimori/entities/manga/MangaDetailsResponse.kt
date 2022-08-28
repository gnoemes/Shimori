package com.gnoemes.shikimori.entities.manga

import com.gnoemes.shikimori.entities.common.*
import com.gnoemes.shikimori.entities.rates.RateResponse
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
internal data class MangaDetailsResponse(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("russian") val nameRu: String?,
    @SerialName("image") val image: ImageResponse,
    @SerialName("url") val url: String,
    @SerialName("kind") val type: ShikimoriMangaType?,
    @SerialName("status") val status: ContentStatus?,
    @SerialName("volumes") val volumes: Int,
    @SerialName("chapters") val chapters: Int,
    @SerialName("aired_on") val dateAired: LocalDate?,
    @SerialName("released_on") val dateReleased: LocalDate?,
    @SerialName("english") val namesEnglish: List<String?>?,
    @SerialName("japanese") val namesJapanese: List<String?>?,
    @SerialName("rating") val ageRating: AgeRating?,
    @SerialName("score") val score: Double,
    @SerialName("description") val description: String?,
    @SerialName("description_html") val descriptionHtml: String,
    @SerialName("franchise") val franchise: String?,
    @SerialName("favoured") val favoured: Boolean,
    @SerialName("topic_id") val topicId: Long?,
    @SerialName("genres") val genres: List<GenreResponse>,
    @SerialName("user_rate") val userRate: RateResponse?,
    @SerialName("rates_scores_stats") val rateScoresStats: List<StatisticResponse>,
    @SerialName("rates_statuses_stats") val rateStatusesStats: List<StatisticResponse>
)