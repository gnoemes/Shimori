package com.gnoemes.shikimori.entities.anime

import com.gnoemes.shikimori.entities.common.GenreResponse
import com.gnoemes.shikimori.entities.common.ImageResponse
import com.gnoemes.shikimori.entities.common.StatisticResponse
import com.gnoemes.shikimori.entities.rates.UserRateResponse
import com.gnoemes.shimori.model.common.AgeRating
import com.gnoemes.shimori.model.common.ContentStatus
import com.google.gson.annotations.SerializedName
import org.threeten.bp.OffsetDateTime

internal data class AnimeDetailsResponse(
    @field:SerializedName("id") val id: Long,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("russian") val nameRu: String?,
    @field:SerializedName("image") val image: ImageResponse,
    @field:SerializedName("url") val url: String,
    @field:SerializedName("kind") val type: ShikimoriAnimeType?,
    @field:SerializedName("status") val status: ContentStatus?,
    @field:SerializedName("episodes") val episodes: Int,
    @field:SerializedName("episodes_aired") val episodesAired: Int,
    @field:SerializedName("aired_on") val dateAired: OffsetDateTime?,
    @field:SerializedName("next_episode_at") val nextEpisodeDate: OffsetDateTime?,
    @field:SerializedName("released_on") val dateReleased: OffsetDateTime?,
    @field:SerializedName("english") val namesEnglish: List<String?>?,
    @field:SerializedName("japanese") val namesJapanese: List<String?>?,
    @field:SerializedName("rating") val ageRating: AgeRating,
    @field:SerializedName("score") val score: Double,
    @field:SerializedName("duration") val duration: Int,
    @field:SerializedName("description") val description: String?,
    @field:SerializedName("description_html") val descriptionHtml: String,
    @field:SerializedName("franchise") val franchise: String?,
    @field:SerializedName("favoured") val favoured: Boolean,
    @field:SerializedName("topic_id") val topicId: Long?,
    @field:SerializedName("genres") val genres: List<GenreResponse>,
    @field:SerializedName("user_rate") val userRate: UserRateResponse?,
    @field:SerializedName("videos") val videoResponses: List<AnimeVideoResponse>?,
    @field:SerializedName("studios") val studioResponses: List<StudioResponse>?,
    @field:SerializedName("rates_scores_stats") val rateScoresStats: List<StatisticResponse>,
    @field:SerializedName("rates_statuses_stats") val rateStatusesStats: List<StatisticResponse>
) 