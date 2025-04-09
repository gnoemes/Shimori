package com.gnoemes.shimori.source.shikimori.mappers

import com.gnoemes.shimori.source.model.SImage
import com.gnoemes.shimori.source.model.STrack
import com.gnoemes.shimori.source.model.STrackStatus
import com.gnoemes.shimori.source.model.SourceDataType
import com.gnoemes.shimori.source.shikimori.AnimeDetailsQuery
import com.gnoemes.shimori.source.shikimori.fragment.AnimeUserRate
import com.gnoemes.shimori.source.shikimori.fragment.AnimeUserRateWithModel
import com.gnoemes.shimori.source.shikimori.fragment.MangaUserRateWithModel
import com.gnoemes.shimori.source.shikimori.fragment.PosterShort
import com.gnoemes.shimori.source.shikimori.type.AnimeKindEnum
import com.gnoemes.shimori.source.shikimori.type.AnimeRatingEnum
import com.gnoemes.shimori.source.shikimori.type.AnimeStatusEnum
import com.gnoemes.shimori.source.shikimori.type.MangaKindEnum
import com.gnoemes.shimori.source.shikimori.type.MangaStatusEnum
import com.gnoemes.shimori.source.shikimori.type.UserRateStatusEnum
import com.gnoemes.shimori.source.shikimori.type.VideoKindEnum
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

fun UserRateStatusEnum.Companion.from(status: STrackStatus?) = when (status) {
    STrackStatus.Watching -> UserRateStatusEnum.watching
    STrackStatus.ReWatching -> UserRateStatusEnum.rewatching
    STrackStatus.Planned -> UserRateStatusEnum.planned
    STrackStatus.Completed -> UserRateStatusEnum.completed
    STrackStatus.Dropped -> UserRateStatusEnum.dropped
    STrackStatus.OnHold -> UserRateStatusEnum.on_hold
    else -> null
}

fun UserRateStatusEnum.toSourceType() = when (this) {
    UserRateStatusEnum.watching -> STrackStatus.Watching
    UserRateStatusEnum.rewatching -> STrackStatus.ReWatching
    UserRateStatusEnum.planned -> STrackStatus.Planned
    UserRateStatusEnum.completed -> STrackStatus.Completed
    UserRateStatusEnum.dropped -> STrackStatus.Dropped
    UserRateStatusEnum.on_hold -> STrackStatus.OnHold
    else -> null
}

fun AnimeKindEnum?.toSourceType() = when (this) {
    AnimeKindEnum.tv -> "tv"
    AnimeKindEnum.ona -> "ona"
    AnimeKindEnum.ova -> "ova"
    AnimeKindEnum.movie -> "movie"
    AnimeKindEnum.special -> "special"
    AnimeKindEnum.music -> "music"
    else -> null
}

fun MangaKindEnum?.toSourceType() = when (this) {
    MangaKindEnum.manga -> rawValue
    MangaKindEnum.manhua -> rawValue
    MangaKindEnum.manhwa -> rawValue
    MangaKindEnum.doujin -> rawValue
    MangaKindEnum.one_shot -> rawValue
    else -> null
}

fun MangaKindEnum?.toSourceRanobeType() = when (this) {
    MangaKindEnum.novel -> rawValue
    MangaKindEnum.light_novel -> rawValue
    else -> null
}

fun AnimeStatusEnum?.toSourceType() = when (this) {
    AnimeStatusEnum.anons -> rawValue
    AnimeStatusEnum.released -> rawValue
    AnimeStatusEnum.ongoing -> rawValue
    else -> null
}

fun MangaStatusEnum?.toSourceType() = when (this) {
    MangaStatusEnum.anons -> rawValue
    MangaStatusEnum.released -> rawValue
    MangaStatusEnum.ongoing -> rawValue
    MangaStatusEnum.discontinued -> rawValue
    MangaStatusEnum.paused -> rawValue
    else -> null
}


fun AnimeRatingEnum?.toSourceType() = when (this) {
    AnimeRatingEnum.g -> rawValue
    AnimeRatingEnum.pg -> rawValue
    AnimeRatingEnum.pg_13 -> rawValue
    AnimeRatingEnum.r -> rawValue
    AnimeRatingEnum.r_plus -> rawValue
    AnimeRatingEnum.rx -> rawValue
    else -> null
}

fun PosterShort?.toSourceType() = this?.let {
    SImage(
        original = it.originalUrl,
        preview = it.mainUrl,
        x96 = it.previewUrl,
    )
}

fun AnimeDetailsQuery.Genre.toSourceType(): String {
    return this.name
}

fun VideoKindEnum?.toSourceType() = when (this) {
    VideoKindEnum.op -> rawValue
    VideoKindEnum.ed -> rawValue
    VideoKindEnum.op_ed_clip -> rawValue
    VideoKindEnum.clip -> rawValue
    VideoKindEnum.character_trailer -> rawValue
    VideoKindEnum.pv -> rawValue
    VideoKindEnum.cm -> rawValue
    VideoKindEnum.episode_preview -> rawValue
    else -> "other"
}

fun AnimeUserRateWithModel?.toSourceType(): STrack? {
    if (this == null) return null

    return STrack(
        id = this.id.toLong(),
        targetId = this.anime!!.animeShort.id.toLong(),
        targetType = SourceDataType.Anime,
        status = this.status.toSourceType()!!,
        score = this.score,
        comment = this.text,
        progress = this.episodes,
        reCounter = this.rewatches,
        dateCreated = this.createdAt.toInstant(),
        dateUpdated = this.updatedAt.toInstant()
    )
}

fun AnimeUserRate?.toSourceType(): STrack? {
    if (this == null) return null

    return STrack(
        id = this.id.toLong(),
        targetId = this.anime!!.id.toLong(),
        targetType = SourceDataType.Anime,
        status = this.status.toSourceType()!!,
        score = this.score,
        comment = this.text,
        progress = this.episodes,
        reCounter = this.rewatches,
        dateCreated = this.createdAt.toInstant(),
        dateUpdated = this.updatedAt.toInstant()
    )
}

fun MangaUserRateWithModel?.toSourceType(isRanobe: Boolean = false): STrack? {
    if (this == null) return null

    return STrack(
        id = this.id.toLong(),
        targetId = this.manga!!.mangaShort.id.toLong(),
        targetType = if (isRanobe) SourceDataType.Ranobe else SourceDataType.Manga,
        status = this.status.toSourceType()!!,
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
