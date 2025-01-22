package com.gnoemes.shimori.sources.shikimori.mappers

import com.gnoemes.shimori.data.common.AgeRating
import com.gnoemes.shimori.data.common.Genre
import com.gnoemes.shimori.data.common.ShimoriImage
import com.gnoemes.shimori.data.common.TitleStatus
import com.gnoemes.shimori.data.titles.anime.AnimeType
import com.gnoemes.shimori.data.titles.anime.AnimeVideoType
import com.gnoemes.shimori.data.titles.manga.MangaType
import com.gnoemes.shimori.data.titles.ranobe.RanobeType
import com.gnoemes.shimori.data.track.Track
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.sources.shikimori.AnimeDetailsQuery
import com.gnoemes.shimori.sources.shikimori.fragment.AnimeUserRate
import com.gnoemes.shimori.sources.shikimori.fragment.AnimeUserRateWithModel
import com.gnoemes.shimori.sources.shikimori.fragment.MangaUserRateWithModel
import com.gnoemes.shimori.sources.shikimori.fragment.PosterShort
import com.gnoemes.shimori.sources.shikimori.type.AnimeKindEnum
import com.gnoemes.shimori.sources.shikimori.type.AnimeRatingEnum
import com.gnoemes.shimori.sources.shikimori.type.AnimeStatusEnum
import com.gnoemes.shimori.sources.shikimori.type.MangaKindEnum
import com.gnoemes.shimori.sources.shikimori.type.MangaStatusEnum
import com.gnoemes.shimori.sources.shikimori.type.UserRateStatusEnum
import com.gnoemes.shimori.sources.shikimori.type.VideoKindEnum
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

fun UserRateStatusEnum.Companion.from(status: TrackStatus?) = when (status) {
    TrackStatus.WATCHING -> UserRateStatusEnum.watching
    TrackStatus.REWATCHING -> UserRateStatusEnum.rewatching
    TrackStatus.PLANNED -> UserRateStatusEnum.planned
    TrackStatus.COMPLETED -> UserRateStatusEnum.completed
    TrackStatus.DROPPED -> UserRateStatusEnum.dropped
    TrackStatus.ON_HOLD -> UserRateStatusEnum.on_hold
    else -> null
}

fun UserRateStatusEnum.toShimoriType() = when (this) {
    UserRateStatusEnum.watching -> TrackStatus.WATCHING
    UserRateStatusEnum.rewatching -> TrackStatus.REWATCHING
    UserRateStatusEnum.planned -> TrackStatus.PLANNED
    UserRateStatusEnum.completed -> TrackStatus.COMPLETED
    UserRateStatusEnum.dropped -> TrackStatus.DROPPED
    UserRateStatusEnum.on_hold -> TrackStatus.ON_HOLD
    else -> null
}

fun AnimeKindEnum?.toShimoriType() = when (this) {
    AnimeKindEnum.tv -> AnimeType.Tv
    AnimeKindEnum.ona -> AnimeType.ONA
    AnimeKindEnum.ova -> AnimeType.OVA
    AnimeKindEnum.movie -> AnimeType.Movie
    AnimeKindEnum.special -> AnimeType.Special
    AnimeKindEnum.music -> AnimeType.Music
    else -> null
}

fun MangaKindEnum?.toShimoriType() = when (this) {
    MangaKindEnum.manga -> MangaType.Manga
    MangaKindEnum.manhua -> MangaType.Manhua
    MangaKindEnum.manhwa -> MangaType.Manhwa
    MangaKindEnum.doujin -> MangaType.Doujin
    MangaKindEnum.one_shot -> MangaType.OneShot
    else -> null
}

fun MangaKindEnum?.toShimoriRanobeType() = when (this) {
    MangaKindEnum.novel -> RanobeType.Novel
    MangaKindEnum.light_novel -> RanobeType.LightNovel
    else -> null
}

fun AnimeStatusEnum?.toShimoriType() = when (this) {
    AnimeStatusEnum.anons -> TitleStatus.ANONS
    AnimeStatusEnum.released -> TitleStatus.RELEASED
    AnimeStatusEnum.ongoing -> TitleStatus.ONGOING
    else -> null
}

fun MangaStatusEnum?.toShimoriType() = when (this) {
    MangaStatusEnum.anons -> TitleStatus.ANONS
    MangaStatusEnum.released -> TitleStatus.RELEASED
    MangaStatusEnum.ongoing -> TitleStatus.ONGOING
    MangaStatusEnum.discontinued -> TitleStatus.DISCONTINUED
    MangaStatusEnum.paused -> TitleStatus.PAUSED
    else -> null
}


fun AnimeRatingEnum?.toShimoriType() = when (this) {
    AnimeRatingEnum.g -> AgeRating.G
    AnimeRatingEnum.pg -> AgeRating.PG
    AnimeRatingEnum.pg_13 -> AgeRating.PG_13
    AnimeRatingEnum.r -> AgeRating.R
    AnimeRatingEnum.r_plus -> AgeRating.R_PLUS
    AnimeRatingEnum.rx -> AgeRating.RX
    else -> AgeRating.NONE
}

fun PosterShort?.toShimoriImage() = this?.let {
    ShimoriImage(
        original = it.originalUrl,
        preview = it.mainUrl,
        x96 = it.previewUrl,
    )
}

fun AnimeDetailsQuery.Genre.toShimoriType(): Genre {
    return Genre(name = this.name, nameRu = this.russian)
}

fun VideoKindEnum?.toShimoriType() = when (this) {
    VideoKindEnum.op -> AnimeVideoType.Opening
    VideoKindEnum.ed -> AnimeVideoType.Ending
    VideoKindEnum.op_ed_clip -> AnimeVideoType.Music
    VideoKindEnum.clip -> AnimeVideoType.Clip
    VideoKindEnum.character_trailer -> AnimeVideoType.CharacterTrailer
    VideoKindEnum.pv -> AnimeVideoType.Promo
    VideoKindEnum.cm -> AnimeVideoType.Commercial
    VideoKindEnum.episode_preview -> AnimeVideoType.EpisodePreview
    else -> AnimeVideoType.Other
}

fun AnimeUserRateWithModel?.toShimoriType(): Track? {
    if (this == null) return null

    return Track(
        id = this.id.toLong(),
        targetId = this.anime!!.animeShort.id.toLong(),
        targetType = TrackTargetType.ANIME,
        status = this.status.toShimoriType()!!,
        score = this.score,
        comment = this.text,
        progress = this.episodes,
        reCounter = this.rewatches,
        dateCreated = this.createdAt.toInstant(),
        dateUpdated = this.updatedAt.toInstant()
    )
}

fun AnimeUserRate?.toShimoriType(): Track? {
    if (this == null) return null

    return Track(
        id = this.id.toLong(),
        targetId = this.anime!!.id.toLong(),
        targetType = TrackTargetType.ANIME,
        status = this.status.toShimoriType()!!,
        score = this.score,
        comment = this.text,
        progress = this.episodes,
        reCounter = this.rewatches,
        dateCreated = this.createdAt.toInstant(),
        dateUpdated = this.updatedAt.toInstant()
    )
}

fun MangaUserRateWithModel?.toShimoriType(isRanobe: Boolean = false): Track? {
    if (this == null) return null

    return Track(
        id = this.id.toLong(),
        targetId = this.manga!!.mangaShort.id.toLong(),
        targetType = if (isRanobe) TrackTargetType.RANOBE else TrackTargetType.MANGA,
        status = this.status.toShimoriType()!!,
        score = this.score,
        comment = this.text,
        progress = this.chapters,
        dateCreated = this.createdAt.toInstant(),
        dateUpdated = this.updatedAt.toInstant()
    )
}

fun Any?.toLocalDate() =
    if (this is String) let(LocalDate::parse)
    else null

fun Any?.toInstant() =
    if (this is String) let(Instant::parse)
    else null
