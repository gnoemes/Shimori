package com.gnoemes.shimori.data.shared

import com.gnoemes.shimori.data.core.entities.PaginatedEntity
import com.gnoemes.shimori.data.core.entities.app.LastRequest
import com.gnoemes.shimori.data.core.entities.characters.Character
import com.gnoemes.shimori.data.core.entities.common.AgeRating
import com.gnoemes.shimori.data.core.entities.common.Genre
import com.gnoemes.shimori.data.core.entities.common.ShimoriImage
import com.gnoemes.shimori.data.core.entities.common.TitleStatus
import com.gnoemes.shimori.data.core.entities.titles.anime.Anime
import com.gnoemes.shimori.data.core.entities.titles.anime.AnimeType
import com.gnoemes.shimori.data.core.entities.titles.anime.AnimeWithTrack
import com.gnoemes.shimori.data.core.entities.titles.manga.Manga
import com.gnoemes.shimori.data.core.entities.titles.manga.MangaType
import com.gnoemes.shimori.data.core.entities.titles.manga.MangaWithTrack
import com.gnoemes.shimori.data.core.entities.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.core.entities.titles.ranobe.RanobeType
import com.gnoemes.shimori.data.core.entities.titles.ranobe.RanobeWithTrack
import com.gnoemes.shimori.data.core.entities.track.*
import com.gnoemes.shimori.data.core.entities.user.User
import com.gnoemes.shimori.data.core.entities.user.UserShort
import com.gnoemes.shimori.data.core.mappers.Mapper
import com.gnoemes.shimori.data.core.mappers.TwoWayMapper
import comgnoemesshimoridatadb.data.QueryMeShort
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

internal val queryMeShortToUserShortMapper = Mapper<QueryMeShort, UserShort> { from ->
    UserShort(
        id = from.id,
        shikimoriId = from.shikimori_id,
        nickname = from.nickname,
        image = ShimoriImage(from.image_original, from.image_preview),
        isMe = from.is_me
    )
}

internal val userToUserShortMapper = Mapper<UserDAO?, UserShort?> { from ->
    if (from == null) return@Mapper null

    UserShort(
        id = from.id,
        shikimoriId = from.shikimori_id,
        nickname = from.nickname,
        image = ShimoriImage(
            original = from.image_original,
            preview = from.image_preview,
            x96 = from.image_x96,
            x48 = from.image_x48
        ),
        isMe = from.is_me
    )
}

internal val trackToSyncMapper = object : TwoWayMapper<TrackToSync, TrackToSyncDAO> {
    override suspend fun map(from: TrackToSync): TrackToSyncDAO {
        return TrackToSyncDAO(
            from.id,
            from.trackId,
            from.targets,
            from.action,
            from.attempts,
            from.lastAttempt
        )
    }

    override suspend fun mapInverse(from: TrackToSyncDAO): TrackToSync {
        return TrackToSync(
            from.id,
            from.track_id,
            from.sync_targets,
            from.sync_action,
            from.attempts,
            from.last_attempt
        )
    }
}

internal object UserMapper : Mapper<UserDAO?, User?> {
    override suspend fun map(from: UserDAO?): User? {
        if (from == null) return null

        return User(
            id = from.id,
            shikimoriId = from.shikimori_id,
            nickname = from.nickname,
            image = ShimoriImage(
                original = from.image_original,
                preview = from.image_preview,
                x96 = from.image_x96,
                x48 = from.image_x48
            ),
            name = from.name,
            about = from.about,
            commonInfo = from.common_info,
            sex = from.sex,
            website = from.website,
            dateBirth = from.date_birth,
            locale = from.locale,
            fullYears = from.full_years,
            location = from.location,
            showComments = from.show_comments,
            friend = from.friend,
            ignored = from.ignored,
            banned = from.banned,
            lastOnlineAt = from.last_online,
            isMe = from.is_me
        )
    }
}

internal object ListSortMapper : Mapper<ListSortDAO?, ListSort?> {
    override suspend fun map(from: ListSortDAO?): ListSort? {
        if (from == null) return null

        return ListSort(
            id = from.id,
            type = ListType.findOrDefault(from.type),
            sortOption = from.sort,
            isDescending = from.descending
        )
    }
}

internal object LastRequestMapper : Mapper<LastRequestDAO?, LastRequest?> {
    override suspend fun map(from: LastRequestDAO?): LastRequest? {
        if (from == null) return null

        return LastRequest(
            id = from.id,
            request = from.request,
            entityId = from.entity_id,
            timeStamp = from.timestamp
        )
    }
}

internal fun animeWithTrack(
    id: Long,
    shikimori_id: Long,
    name: String,
    name_ru: String?,
    name_eng: String?,
    image_original: String?,
    image_preview: String?,
    image_x96: String?,
    image_x48: String?,
    url: String?,
    anime_type: String?,
    rating: Double?,
    status: TitleStatus?,
    episodes: Int,
    episodes_aired: Int,
    date_aired: LocalDate?,
    date_released: LocalDate?,
    age_rating: AgeRating,
    description: String?,
    description_html: String?,
    franchise: String?,
    favorite: Boolean,
    topic_id: Long?,
    genres: List<Genre>?,
    duration: Int?,
    next_episode: Int?,
    next_episode_date: Instant?,
    next_episode_end_date: Instant?,
    id_: Long?,
    shikimori_id_: Long?,
    target_id: Long?,
    target_type: TrackTargetType?,
    target_shikimori_id: Long?,
    status_: TrackStatus?,
    score: Int?,
    comment: String?,
    progress: Int?,
    re_counter: Int?,
    date_created: Instant?,
    date_updated: Instant?,
    pinId: Long? = null,
    target_id_: Long? = null,
    target_type_: TrackTargetType? = null,
) = AnimeWithTrack(
    entity = anime(
        id, shikimori_id, name, name_ru, name_eng,
        image_original, image_preview, image_x96, image_x48,
        url, anime_type, rating, status, episodes, episodes_aired,
        date_aired, date_released, age_rating, description, description_html,
        franchise, favorite, topic_id, genres,
        duration, next_episode, next_episode_date, next_episode_end_date,
    ),
    track = if (id_ == null || target_id == null || target_type == null || status_ == null || progress == null || re_counter == null) null
    else track(
        id_, shikimori_id_, target_id, target_type, target_shikimori_id, status_, score,
        comment, progress, re_counter, date_created, date_updated
    ),
    pinned = pinId != null
)

internal fun anime(
    id: Long,
    shikimori_id: Long,
    name: String,
    name_ru: String?,
    name_eng: String?,
    image_original: String?,
    image_preview: String?,
    image_x96: String?,
    image_x48: String?,
    url: String?,
    anime_type: String?,
    rating: Double?,
    status: TitleStatus?,
    episodes: Int,
    episodes_aired: Int,
    date_aired: LocalDate?,
    date_released: LocalDate?,
    age_rating: AgeRating,
    description: String?,
    description_html: String?,
    franchise: String?,
    favorite: Boolean,
    topic_id: Long?,
    genres: List<Genre>?,
    duration: Int?,
    next_episode: Int?,
    next_episode_date: Instant?,
    next_episode_end_date: Instant?,
) = Anime(
    id = id,
    shikimoriId = shikimori_id,
    name = name,
    nameRu = name_ru,
    nameEn = name_eng,
    image = ShimoriImage(
        original = image_original,
        preview = image_preview,
        x96 = image_x96,
        x48 = image_x48
    ),
    url = url,
    animeType = AnimeType.find(anime_type),
    rating = rating,
    status = status,
    episodes = episodes,
    episodesAired = episodes_aired,
    dateAired = date_aired,
    dateReleased = date_released,
    nextEpisode = next_episode,
    nextEpisodeDate = next_episode_date,
    nextEpisodeEndDate = next_episode_end_date,
    ageRating = age_rating,
    duration = duration,
    description = description,
    descriptionHtml = description_html,
    franchise = franchise,
    favorite = favorite,
    topicId = topic_id,
    genres = genres
)

val animeListViewMapper: (
    id: Long,
    name: String,
    nameRu: String?,
    nameEn: String?,
    image_original: String?,
    image_preview: String?,
    image_x96: String?,
    image_x48: String?,
    anime_type: String?,
    rating: Double?,
    status: TitleStatus?,
    episodes: Int,
    episodes_aired: Int,
    date_aired: LocalDate?,
    date_released: LocalDate?,
    duration: Int?,
    next_episode: Int?,
    next_episode_date: Instant?,
    next_episode_end_date: Instant?,
    trackId: Long,
    trackStatus: TrackStatus,
    score: Int?,
    comment: String?,
    progress: Int,
    re_counter: Int,
    date_created: Instant?,
    date_updated: Instant?,
    pinId: Long?
) -> AnimeWithTrack = { id, name, nameRu, nameEn,
                        imageOriginal, imagePreview, imageX96, imageX48,
                        animeType, rating, status, episodes, episodesAired,
                        dateAired, dateReleased, duration, nextEpisode, nextEpisodeDate, nextEpisodeEndDate,
                        trackId, trackStatus, score, comment, progress, reCounter, dateCreated, dateUpdated, pinId ->
    animeWithTrack(
        id = id,
        shikimori_id = 0,
        name = name,
        name_ru = nameRu,
        name_eng = nameEn,
        image_original = imageOriginal,
        image_preview = imagePreview,
        image_x96 = imageX96,
        image_x48 = imageX48,
        url = null,
        anime_type = animeType,
        rating = rating,
        status = status,
        episodes = episodes,
        episodes_aired = episodesAired,
        date_aired = dateAired,
        date_released = dateReleased,
        age_rating = AgeRating.NONE,
        description = null,
        description_html = null,
        franchise = null,
        favorite = false,
        topic_id = null,
        genres = null,
        duration = duration,
        next_episode = nextEpisode,
        next_episode_date = nextEpisodeDate,
        next_episode_end_date = nextEpisodeEndDate,
        id_ = trackId,
        shikimori_id_ = null,
        target_id = id,
        target_type = TrackTargetType.ANIME,
        target_shikimori_id = null,
        status_ = trackStatus,
        score = score,
        comment = comment,
        progress = progress,
        re_counter = reCounter,
        date_created = dateCreated,
        date_updated = dateUpdated,
        pinId = pinId,
        target_id_ = id,
        target_type_ = TrackTargetType.ANIME
    )
}

internal fun mangaWithTrack(
    id: Long,
    shikimori_id: Long,
    name: String,
    name_ru: String?,
    name_eng: String?,
    image_original: String?,
    image_preview: String?,
    image_x96: String?,
    image_x48: String?,
    url: String?,
    manga_type: String?,
    rating: Double?,
    status: TitleStatus?,
    chapters: Int,
    volumes: Int,
    date_aired: LocalDate?,
    date_released: LocalDate?,
    age_rating: AgeRating,
    description: String?,
    description_html: String?,
    franchise: String?,
    favorite: Boolean,
    topic_id: Long?,
    genres: List<Genre>?,
    id_: Long?,
    shikimori_id_: Long?,
    target_id: Long?,
    target_type: TrackTargetType?,
    target_shikimori_id: Long?,
    status_: TrackStatus?,
    score: Int?,
    comment: String?,
    progress: Int?,
    re_counter: Int?,
    date_created: Instant?,
    date_updated: Instant?,
    pinId: Long? = null,
    target_id_: Long? = null,
    target_type_: TrackTargetType? = null
) = MangaWithTrack(
    entity = manga(
        id, shikimori_id, name, name_ru, name_eng,
        image_original, image_preview, image_x96, image_x48,
        url, manga_type, rating, status, chapters, volumes,
        date_aired, date_released, age_rating, description,
        description_html, franchise, favorite, topic_id, genres
    ),
    track = if (id_ == null || target_id == null || target_type == null || status_ == null || progress == null || re_counter == null) null
    else track(
        id_, shikimori_id_, target_id, target_type, target_shikimori_id, status_, score,
        comment, progress, re_counter, date_created, date_updated
    ),
    pinned = pinId != null,
)

internal fun manga(
    id: Long,
    shikimori_id: Long,
    name: String,
    name_ru: String?,
    name_eng: String?,
    image_original: String?,
    image_preview: String?,
    image_x96: String?,
    image_x48: String?,
    url: String?,
    manga_type: String?,
    rating: Double?,
    status: TitleStatus?,
    chapters: Int,
    volumes: Int,
    date_aired: LocalDate?,
    date_released: LocalDate?,
    age_rating: AgeRating,
    description: String?,
    description_html: String?,
    franchise: String?,
    favorite: Boolean,
    topic_id: Long?,
    genres: List<Genre>?,
) = Manga(
    id = id,
    shikimoriId = shikimori_id,
    name = name,
    nameRu = name_ru,
    nameEn = name_eng,
    image = ShimoriImage(
        original = image_original,
        preview = image_preview,
        x96 = image_x96,
        x48 = image_x48
    ),
    url = url,
    mangaType = MangaType.find(manga_type),
    rating = rating,
    status = status,
    chapters = chapters,
    volumes = volumes,
    dateAired = date_aired,
    dateReleased = date_released,
    ageRating = age_rating,
    description = description,
    descriptionHtml = description_html,
    franchise = franchise,
    favorite = favorite,
    topicId = topic_id,
    genres = genres
)

val mangaListViewMapper: (
    id: Long,
    name: String,
    nameRu: String?,
    nameEn: String?,
    image_original: String?,
    image_preview: String?,
    image_x96: String?,
    image_x48: String?,
    manga_type: String?,
    rating: Double?,
    status: TitleStatus?,
    volumes: Int,
    chapters: Int,
    date_aired: LocalDate?,
    date_released: LocalDate?,
    trackId: Long,
    trackStatus: TrackStatus,
    score: Int?,
    comment: String?,
    progress: Int,
    re_counter: Int,
    date_created: Instant?,
    date_updated: Instant?,
    pinId: Long?
) -> MangaWithTrack = { id, name, nameRu, nameEn,
                        imageOriginal, imagePreview, imageX96, imageX48,
                        manga_type, rating, status, volumes, chapters,
                        dateAired, dateReleased, trackId, trackStatus, score, comment,
                        progress, reCounter, dateCreated, dateUpdated, pinId ->
    mangaWithTrack(
        id = id,
        shikimori_id = 0,
        name = name,
        name_ru = nameRu,
        name_eng = nameEn,
        image_original = imageOriginal,
        image_preview = imagePreview,
        image_x96 = imageX96,
        image_x48 = imageX48,
        url = null,
        manga_type = manga_type,
        rating = rating,
        status = status,
        volumes = volumes,
        chapters = chapters,
        date_aired = dateAired,
        date_released = dateReleased,
        age_rating = AgeRating.NONE,
        description = null,
        description_html = null,
        franchise = null,
        favorite = false,
        topic_id = null,
        genres = null,
        id_ = trackId,
        shikimori_id_ = null,
        target_id = id,
        target_type = TrackTargetType.MANGA,
        target_shikimori_id = null,
        status_ = trackStatus,
        score = score,
        comment = comment,
        progress = progress,
        re_counter = reCounter,
        date_created = dateCreated,
        date_updated = dateUpdated,
        pinId = pinId,
        target_id_ = id,
        target_type_ = TrackTargetType.MANGA
    )
}

internal fun ranobeWithTrack(
    id: Long,
    shikimori_id: Long,
    name: String,
    name_ru: String?,
    name_eng: String?,
    image_original: String?,
    image_preview: String?,
    image_x96: String?,
    image_x48: String?,
    url: String?,
    ranobe_type: String?,
    rating: Double?,
    status: TitleStatus?,
    chapters: Int,
    volumes: Int,
    date_aired: LocalDate?,
    date_released: LocalDate?,
    age_rating: AgeRating,
    description: String?,
    description_html: String?,
    franchise: String?,
    favorite: Boolean,
    topic_id: Long?,
    genres: List<Genre>?,
    id_: Long?,
    shikimori_id_: Long?,
    target_id: Long?,
    target_type: TrackTargetType?,
    target_shikimori_id: Long?,
    status_: TrackStatus?,
    score: Int?,
    comment: String?,
    progress: Int?,
    re_counter: Int?,
    date_created: Instant?,
    date_updated: Instant?,
    pinId: Long? = null,
    target_id_: Long? = null,
    target_type_: TrackTargetType? = null
) = RanobeWithTrack(
    entity = ranobe(
        id, shikimori_id, name, name_ru, name_eng,
        image_original, image_preview, image_x96, image_x48,
        url, ranobe_type, rating, status, chapters, volumes,
        date_aired, date_released, age_rating, description,
        description_html, franchise, favorite, topic_id, genres
    ),
    track = if (id_ == null || target_id == null || target_type == null || status_ == null || progress == null || re_counter == null) null
    else track(
        id_, shikimori_id_, target_id, target_type, target_shikimori_id, status_, score,
        comment, progress, re_counter, date_created, date_updated
    ),
    pinned = pinId != null
)

internal fun ranobe(
    id: Long,
    shikimori_id: Long,
    name: String,
    name_ru: String?,
    name_eng: String?,
    image_original: String?,
    image_preview: String?,
    image_x96: String?,
    image_x48: String?,
    url: String?,
    ranobe_type: String?,
    rating: Double?,
    status: TitleStatus?,
    chapters: Int,
    volumes: Int,
    date_aired: LocalDate?,
    date_released: LocalDate?,
    age_rating: AgeRating,
    description: String?,
    description_html: String?,
    franchise: String?,
    favorite: Boolean,
    topic_id: Long?,
    genres: List<Genre>?,
) = Ranobe(
    id = id,
    shikimoriId = shikimori_id,
    name = name,
    nameRu = name_ru,
    nameEn = name_eng,
    image = ShimoriImage(
        original = image_original,
        preview = image_preview,
        x96 = image_x96,
        x48 = image_x48
    ),
    url = url,
    ranobeType = RanobeType.find(ranobe_type),
    rating = rating,
    status = status,
    chapters = chapters,
    volumes = volumes,
    dateAired = date_aired,
    dateReleased = date_released,
    ageRating = age_rating,
    description = description,
    descriptionHtml = description_html,
    franchise = franchise,
    favorite = favorite,
    topicId = topic_id,
    genres = genres
)

val ranobeListViewMapper: (
    id: Long,
    name: String,
    nameRu: String?,
    nameEn: String?,
    image_original: String?,
    image_preview: String?,
    image_x96: String?,
    image_x48: String?,
    ranobe_type: String?,
    rating: Double?,
    status: TitleStatus?,
    volumes: Int,
    chapters: Int,
    date_aired: LocalDate?,
    date_released: LocalDate?,
    trackId: Long,
    trackStatus: TrackStatus,
    score: Int?,
    comment: String?,
    progress: Int,
    re_counter: Int,
    date_created: Instant?,
    date_updated: Instant?,
    pinId: Long?
) -> RanobeWithTrack = { id, name, nameRu, nameEn,
                         imageOriginal, imagePreview, imageX96, imageX48,
                         ranobe_type, rating, status, volumes, chapters,
                         dateAired, dateReleased, trackId, trackStatus, score, comment,
                         progress, reCounter, dateCreated, dateUpdated, pinId ->
    ranobeWithTrack(
        id = id,
        shikimori_id = 0,
        name = name,
        name_ru = nameRu,
        name_eng = nameEn,
        image_original = imageOriginal,
        image_preview = imagePreview,
        image_x96 = imageX96,
        image_x48 = imageX48,
        url = null,
        ranobe_type = ranobe_type,
        rating = rating,
        status = status,
        volumes = volumes,
        chapters = chapters,
        date_aired = dateAired,
        date_released = dateReleased,
        age_rating = AgeRating.NONE,
        description = null,
        description_html = null,
        franchise = null,
        favorite = false,
        topic_id = null,
        genres = null,
        id_ = trackId,
        shikimori_id_ = null,
        target_id = id,
        target_type = TrackTargetType.MANGA,
        target_shikimori_id = null,
        status_ = trackStatus,
        score = score,
        comment = comment,
        progress = progress,
        re_counter = reCounter,
        date_created = dateCreated,
        date_updated = dateUpdated,
        pinId = pinId,
        target_id_ = id,
        target_type_ = TrackTargetType.MANGA
    )
}

internal fun track(
    id: Long,
    shikimori_id: Long?,
    target_id: Long,
    target_type: TrackTargetType,
    target_shikimori_id: Long?,
    status: TrackStatus,
    score: Int?,
    comment: String?,
    progress: Int,
    re_counter: Int,
    date_created: Instant?,
    date_updated: Instant?
) = Track(
    id = id,
    shikimoriId = shikimori_id ?: 0,
    targetId = target_id,
    targetType = target_type,
    targetShikimoriId = target_shikimori_id,
    status = status,
    score = score,
    comment = comment,
    progress = progress,
    reCounter = re_counter,
    dateCreated = date_created,
    dateUpdated = date_updated,
)


internal fun pinPaginated(
    id: Long,
    shikimori_id: Long,
    name: String,
    name_ru: String?,
    name_eng: String?,
    image_original: String?,
    image_preview: String?,
    image_x96: String?,
    image_x48: String?,
    url: String?,
    anime_type: String?,
    rating: Double?,
    status: TitleStatus?,
    episodes: Int,
    episodes_aired: Int,
    date_aired: LocalDate?,
    date_released: LocalDate?,
    age_rating: AgeRating,
    description: String?,
    description_html: String?,
    franchise: String?,
    favorite: Boolean,
    topic_id: Long?,
    genres: List<Genre>?,
    duration: Int?,
    next_episode: Int?,
    next_episode_date: Instant?,
    next_episode_end_date: Instant?,
    id_: Long,
    shikimori_id_: Long?,
    target_id: Long,
    target_type: TrackTargetType,
    target_shikimori_id: Long?,
    status_: TrackStatus,
    score: Int?,
    comment: String?,
    progress: Int,
    re_counter: Int,
    date_created: Instant?,
    date_updated: Instant?,
    id__: Long,
    target_id_: Long,
    target_type_: TrackTargetType
): PaginatedEntity {
    return when (target_type) {
        TrackTargetType.ANIME -> animeWithTrack(
            id,
            shikimori_id,
            name,
            name_ru,
            name_eng,
            image_original,
            image_preview,
            image_x96,
            image_x48,
            url,
            anime_type,
            rating,
            status,
            episodes,
            episodes_aired,
            date_aired,
            date_released,
            age_rating,
            description,
            description_html,
            franchise,
            favorite,
            topic_id,
            genres,
            duration,
            next_episode,
            next_episode_date,
            next_episode_end_date,
            id_,
            shikimori_id_,
            target_id,
            target_type,
            target_shikimori_id,
            status_,
            score,
            comment,
            progress,
            re_counter,
            date_created,
            date_updated,
            id__,
            target_id_,
            target_type_
        )
        TrackTargetType.MANGA -> mangaWithTrack(
            id,
            shikimori_id,
            name,
            name_ru,
            name_eng,
            image_original,
            image_preview,
            image_x96,
            image_x48,
            url,
            anime_type,
            rating,
            status,
            episodes,
            episodes_aired,
            date_aired,
            date_released,
            age_rating,
            description,
            description_html,
            franchise,
            favorite,
            topic_id,
            genres,
            id_,
            shikimori_id_,
            target_id,
            target_type,
            target_shikimori_id,
            status_,
            score,
            comment,
            progress,
            re_counter,
            date_created,
            date_updated,
            id__,
            target_id_,
            target_type_
        )
        TrackTargetType.RANOBE -> ranobeWithTrack(
            id,
            shikimori_id,
            name,
            name_ru,
            name_eng,
            image_original,
            image_preview,
            image_x96,
            image_x48,
            url,
            anime_type,
            rating,
            status,
            episodes,
            episodes_aired,
            date_aired,
            date_released,
            age_rating,
            description,
            description_html,
            franchise,
            favorite,
            topic_id,
            genres,
            id_,
            shikimori_id_,
            target_id,
            target_type,
            target_shikimori_id,
            status_,
            score,
            comment,
            progress,
            re_counter,
            date_created,
            date_updated,
            id__,
            target_id_,
            target_type_
        )
    }
}

internal fun character(
    id: Long,
    shikimori_id: Long,
    name: String,
    name_ru: String?,
    name_eng: String?,
    image_original: String?,
    image_preview: String?,
    image_x96: String?,
    image_x48: String?,
    url: String?,
    description: String?,
    description_source_url: String?
): Character {
    return Character(
        id = id,
        shikimoriId = shikimori_id,
        name = name,
        nameRu = name_ru,
        nameEn = name_eng,
        image = ShimoriImage(
            original = image_original,
            preview = image_preview,
            x96 = image_x96,
            x48 = image_x48
        ),
        url = url,
        description = description,
        descriptionSourceUrl = description_source_url
    )
}