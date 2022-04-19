package com.gnoemes.shikimori.entities.anime

import com.gnoemes.shikimori.entities.common.*
import com.gnoemes.shikimori.entities.common.GenreResponse
import com.gnoemes.shikimori.entities.common.ImageResponse
import com.gnoemes.shikimori.entities.common.StatisticResponse
import com.gnoemes.shikimori.entities.rates.UserRateResponse
import kotlinx.datetime.DateTimePeriod
import kotlinx.serialization.SerialName


@kotlinx.serialization.Serializable
internal data class AnimeDetailsResponse(
    @SerialName("id") val id: Long,
    @SerialName("name") val name: String,
    @SerialName("russian") val nameRu: String?,
    @SerialName("image") val image: ImageResponse,
    @SerialName("url") val url: String,
    @SerialName("kind") val type: ShikimoriAnimeType?,
    @SerialName("status") val status: ContentStatus?,
    @SerialName("episodes") val episodes: Int,
    @SerialName("episodes_aired") val episodesAired: Int,
    @SerialName("aired_on") val dateAired: DateTimePeriod?,
    @SerialName("next_episode_at") val nextEpisodeDate: DateTimePeriod?,
    @SerialName("released_on") val dateReleased: DateTimePeriod?,
    @SerialName("english") val namesEnglish: List<String?>?,
    @SerialName("japanese") val namesJapanese: List<String?>?,
    @SerialName("rating") val ageRating: AgeRating,
    @SerialName("score") val score: Double,
    @SerialName("duration") val duration: Int,
    @SerialName("description") val description: String?,
    @SerialName("description_html") val descriptionHtml: String,
    @SerialName("franchise") val franchise: String?,
    @SerialName("favoured") val favoured: Boolean,
    @SerialName("topic_id") val topicId: Long?,
    @SerialName("genres") val genres: List<GenreResponse>,
    @SerialName("user_rate") val userRate: UserRateResponse?,
    @SerialName("videos") val videoResponses: List<AnimeVideoResponse>?,
    @SerialName("studios") val studioResponses: List<StudioResponse>?,
    @SerialName("rates_scores_stats") val rateScoresStats: List<StatisticResponse>,
    @SerialName("rates_statuses_stats") val rateStatusesStats: List<StatisticResponse>
) 