package com.gnoemes.shimori.data.util

import com.gnoemes.shimori.base.utils.Mapper
import com.gnoemes.shimori.base.utils.TwoWayMapper
import com.gnoemes.shimori.data.PaginatedEntity
import com.gnoemes.shimori.data.adapters.GenreIdsAdapter
import com.gnoemes.shimori.data.app.LastRequest
import com.gnoemes.shimori.data.characters.Character
import com.gnoemes.shimori.data.characters.CharacterRole
import com.gnoemes.shimori.data.characters.CharacterWithRole
import com.gnoemes.shimori.data.common.AgeRating
import com.gnoemes.shimori.data.common.Genre
import com.gnoemes.shimori.data.common.GenreRelation
import com.gnoemes.shimori.data.common.GenreType
import com.gnoemes.shimori.data.common.ShimoriImage
import com.gnoemes.shimori.data.common.Studio
import com.gnoemes.shimori.data.common.TitleStatus
import com.gnoemes.shimori.data.person.Person
import com.gnoemes.shimori.data.titles.anime.Anime
import com.gnoemes.shimori.data.titles.anime.AnimeScreenshot
import com.gnoemes.shimori.data.titles.anime.AnimeType
import com.gnoemes.shimori.data.titles.anime.AnimeVideo
import com.gnoemes.shimori.data.titles.anime.AnimeVideoType
import com.gnoemes.shimori.data.titles.anime.AnimeWithTrack
import com.gnoemes.shimori.data.titles.manga.Manga
import com.gnoemes.shimori.data.titles.manga.MangaType
import com.gnoemes.shimori.data.titles.manga.MangaWithTrack
import com.gnoemes.shimori.data.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.titles.ranobe.RanobeType
import com.gnoemes.shimori.data.titles.ranobe.RanobeWithTrack
import com.gnoemes.shimori.data.track.ListSort
import com.gnoemes.shimori.data.track.Track
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.data.track.TrackToSync
import com.gnoemes.shimori.data.user.User
import com.gnoemes.shimori.data.user.UserShort
import comgnoemesshimori.data.QueryMeShort
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate

internal val queryMeShortToUserShortMapper = Mapper<QueryMeShort, UserShort> { from ->
    UserShort(
        id = from.id,
        remoteId = from.remote_id,
        sourceId = from.source_id,
        nickname = from.nickname,
        image = ShimoriImage(from.image_original, from.image_preview),
        isMe = from.is_me
    )
}

internal val userToUserShortMapper = Mapper<UserDAO?, UserShort?> { from ->
    if (from == null) return@Mapper null

    UserShort(
        id = from.id,
        remoteId = from.remote_id,
        sourceId = from.source_id,
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
    override fun map(from: TrackToSync): TrackToSyncDAO {
        return TrackToSyncDAO(
            from.id,
            from.trackId,
            from.action,
            from.attempts,
            from.lastAttempt,
            from.attemptSourceId
        )
    }

    override fun mapInverse(from: TrackToSyncDAO): TrackToSync {
        return TrackToSync(
            from.id,
            from.track_id,
            from.sync_action,
            from.attempts,
            from.last_attempt,
            from.attempt_source_id
        )
    }
}

internal object UserMapper : Mapper<UserDAO?, User?> {
    override fun map(from: UserDAO?): User? {
        if (from == null) return null

        return User(
            id = from.id,
            remoteId = from.remote_id,
            sourceId = from.source_id,
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
    override fun map(from: ListSortDAO?): ListSort? {
        if (from == null) return null

        return ListSort(
            id = from.id,
            type = from.type,
            sortOption = from.sort,
            isDescending = from.descending
        )
    }
}

internal object LastRequestMapper : Mapper<LastRequestDAO?, LastRequest?> {
    override fun map(from: LastRequestDAO?): LastRequest? {
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
    duration: Int?,
    next_episode: Int?,
    next_episode_date: Instant?,
    dubbers: String?,
    subbers: String?,
    id_: Long?,
    target_id: Long?,
    target_type: TrackTargetType?,
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
        id, name, name_ru, name_eng,
        image_original, image_preview, image_x96, image_x48,
        url, anime_type, rating, status, episodes, episodes_aired,
        date_aired, date_released, age_rating, description, description_html,
        franchise, favorite, topic_id,
        duration, next_episode, next_episode_date, dubbers, subbers
    ),
    track = if (id_ == null || target_id == null || target_type == null || status_ == null || progress == null || re_counter == null) null
    else track(
        id_, target_id, target_type, status_, score,
        comment, progress, re_counter, date_created, date_updated
    ),
    pinned = pinId != null
)

internal fun anime(
    id: Long,
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
    duration: Int?,
    next_episode: Int?,
    next_episode_date: Instant?,
    dubbers: String?,
    subbers: String?,
) = Anime(

    id = id,
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
    ageRating = age_rating,
    duration = duration,
    description = description,
    descriptionHtml = description_html,
    franchise = franchise,
    favorite = favorite,
    topicId = topic_id,
    dubbers = dubbers?.split(", "),
    subbers = subbers?.split(", "),
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
                        dateAired, dateReleased, duration, nextEpisode, nextEpisodeDate,
                        trackId, trackStatus, score, comment, progress, reCounter, dateCreated, dateUpdated, pinId ->
    animeWithTrack(
        id = id,
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
        duration = duration,
        next_episode = nextEpisode,
        next_episode_date = nextEpisodeDate,
        dubbers = null,
        subbers = null,
        id_ = trackId,
        target_id = id,
        target_type = TrackTargetType.ANIME,
        status_ = trackStatus,
        score = score,
        comment = comment,
        progress = progress,
        re_counter = reCounter,
        date_created = dateCreated,
        date_updated = dateUpdated,
        pinId = pinId,
        target_id_ = id,
        target_type_ = TrackTargetType.ANIME,
    )
}

internal fun mangaWithTrack(
    id: Long,
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
    id_: Long?,
    target_id: Long?,
    target_type: TrackTargetType?,
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
        id, name, name_ru, name_eng,
        image_original, image_preview, image_x96, image_x48,
        url, manga_type, rating, status, chapters, volumes,
        date_aired, date_released, age_rating, description,
        description_html, franchise, favorite, topic_id,
    ),
    track = if (id_ == null || target_id == null || target_type == null || status_ == null || progress == null || re_counter == null) null
    else track(
        id_, target_id, target_type, status_, score,
        comment, progress, re_counter, date_created, date_updated
    ),
    pinned = pinId != null,
)

internal fun manga(
    id: Long,
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
) = Manga(
    id = id,
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
        id_ = trackId,
        target_id = id,
        target_type = TrackTargetType.MANGA,
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
    id_: Long?,
    target_id: Long?,
    target_type: TrackTargetType?,
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
        id, name, name_ru, name_eng,
        image_original, image_preview, image_x96, image_x48,
        url, ranobe_type, rating, status, chapters, volumes,
        date_aired, date_released, age_rating, description,
        description_html, franchise, favorite, topic_id
    ),
    track = if (id_ == null || target_id == null || target_type == null || status_ == null || progress == null || re_counter == null) null
    else track(
        id_, target_id, target_type, status_, score,
        comment, progress, re_counter, date_created, date_updated
    ),
    pinned = pinId != null
)

internal fun ranobe(
    id: Long,
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
) = Ranobe(
    id = id,
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
        id_ = trackId,
        target_id = id,
        target_type = TrackTargetType.MANGA,
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
    target_id: Long,
    target_type: TrackTargetType,
    status: TrackStatus,
    score: Int?,
    comment: String?,
    progress: Int,
    re_counter: Int,
    date_created: Instant?,
    date_updated: Instant?
) = Track(
    id = id,
    targetId = target_id,
    targetType = target_type,
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
    name: String,
    nameRu: String?,
    nameEn: String?,
    image_original: String?,
    image_preview: String?,
    image_x96: String?,
    image_x48: String?,
    type: String?,
    rating: Double?,
    status: TitleStatus?,
    episodes: Int?,
    episodes_aired: Int?,
    volumes: Int?,
    chapters: Int?,
    date_aired: LocalDate?,
    date_released: LocalDate?,
    duration: Int?,
    next_episode: Int?,
    next_episode_date: Instant?,
    trackId: Long,
    trackStatus: TrackStatus,
    target_type: TrackTargetType,
    score: Int?,
    comment: String?,
    progress: Int,
    re_counter: Int,
    date_created: Instant?,
    date_updated: Instant?,
    pinId: Long?
): PaginatedEntity {
    return when (target_type) {
        TrackTargetType.ANIME -> animeWithTrack(
            id,
            name,
            nameRu,
            nameEn,
            image_original,
            image_preview,
            image_x96,
            image_x48,
            null,
            type,
            rating,
            status,
            episodes ?: 0,
            episodes_aired ?: 0,
            date_aired,
            date_released,
            AgeRating.NONE,
            null,
            null,
            null,
            false,
            null,
            duration,
            next_episode,
            next_episode_date,
            null,
            null,
            trackId,
            id,
            target_type,
            trackStatus,
            score,
            comment,
            progress,
            re_counter,
            date_created,
            date_updated,
            pinId,
            id,
            target_type
        )

        TrackTargetType.MANGA -> mangaWithTrack(
            id,
            name,
            nameRu,
            nameEn,
            image_original,
            image_preview,
            image_x96,
            image_x48,
            null,
            type,
            rating,
            status,
            chapters ?: 0,
            volumes ?: 0,
            date_aired,
            date_released,
            AgeRating.NONE,
            null,
            null,
            null,
            false,
            null,
            trackId,
            id,
            target_type,
            trackStatus,
            score,
            comment,
            progress,
            re_counter,
            date_created,
            date_updated,
            pinId,
            id,
            target_type
        )

        TrackTargetType.RANOBE -> ranobeWithTrack(
            id,
            name,
            nameRu,
            nameEn,
            image_original,
            image_preview,
            image_x96,
            image_x48,
            null,
            type,
            rating,
            status,
            chapters ?: 0,
            volumes ?: 0,
            date_aired,
            date_released,
            AgeRating.NONE,
            null,
            null,
            null,
            false,
            null,
            trackId,
            id,
            target_type,
            trackStatus,
            score,
            comment,
            progress,
            re_counter,
            date_created,
            date_updated,
            pinId,
            id,
            target_type
        )
    }
}

internal fun character(
    id: Long,
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

internal fun characterWithRole(
    id: Long,
    name: String,
    name_ru: String?,
    name_eng: String?,
    image_original: String?,
    image_preview: String?,
    image_x96: String?,
    image_x48: String?,
    roleId: Long,
    role: String?,
    roleRu: String?,
    target_id: Long,
    target_type: TrackTargetType
): CharacterWithRole {
    val character = Character(
        id = id,
        name = name,
        nameRu = name_ru,
        nameEn = name_eng,
        image = ShimoriImage(
            original = image_original,
            preview = image_preview,
            x96 = image_x96,
            x48 = image_x48
        ),
    )
    val role = CharacterRole(
        id = roleId,
        characterId = id,
        targetId = target_id,
        targetType = target_type,
        role = role,
        roleRu = roleRu
    )

    return CharacterWithRole(character, role)
}

internal fun characterRole(
    id: Long,
    characterId: Long,
    targetId: Long,
    target_type: TrackTargetType,
    role: String?,
    roleRu: String?,
): CharacterRole {
    return CharacterRole(
        id = id,
        characterId = characterId,
        targetId = targetId,
        targetType = target_type,
        role = role,
        roleRu = roleRu,
    )
}

internal fun video(
    id: Long,
    title_id: Long,
    name: String?,
    url: String,
    image_url: String?,
    type: Int?,
    hosting: String?
): AnimeVideo {
    return AnimeVideo(
        id = id,
        titleId = title_id,
        name = name,
        url = url,
        imageUrl = image_url,
        type = AnimeVideoType.find(type),
        hosting = hosting
    )
}

internal fun screenshot(
    id: Long,
    title_id: Long,
    original: String,
    preview: String
): AnimeScreenshot {
    return AnimeScreenshot(
        id = id,
        titleId = title_id,
        image = ShimoriImage(
            original = original,
            preview = preview
        )
    )
}

internal fun genre(
    id: Long,
    source_id: Long,
    type: GenreType,
    name: String,
    name_ru: String?,
    description: String?,
): Genre {
    return Genre(
        id, source_id, type, name, name_ru, description
    )
}

internal fun genreRelation(
    id: Long,
    targetId: Long,
    type: TrackTargetType,
    source_id: Long,
    ids: String
): GenreRelation {
    return GenreRelation(
        id = id,
        sourceId = source_id,
        targetId = targetId,
        type,
        GenreIdsAdapter.decode(ids)
    )
}

internal fun studio(
    id: Long,
    source_id: Long,
    name: String,
    imageUrl: String?,
): Studio {
    return Studio(
        id = id,
        sourceId = source_id,
        name = name,
        imageUrl = imageUrl
    )
}

internal fun person(
    id: Long,
    name: String,
    name_ru: String?,
    name_eng: String?,
    image_original: String?,
    image_preview: String?,
    image_x96: String?,
    image_x48: String?,
    url: String?,
    is_mangaka: Boolean,
    is_producer: Boolean,
    is_seyu: Boolean,
    birthday_date: LocalDate?,
    deceased_date: LocalDate?,
): Person {
    return Person(
        id = id,
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
        isMangaka = is_mangaka,
        isProducer = is_producer,
        isSeyu = is_seyu,
        birthDate = birthday_date,
        deceasedDate = deceased_date
    )
}
