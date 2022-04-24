package com.gnoemes.shimori.data.shared

import com.gnoemes.shimori.data.base.entities.app.LastRequest
import com.gnoemes.shimori.data.base.entities.common.AgeRating
import com.gnoemes.shimori.data.base.entities.common.Genre
import com.gnoemes.shimori.data.base.entities.common.ShimoriImage
import com.gnoemes.shimori.data.base.entities.common.TitleStatus
import com.gnoemes.shimori.data.base.entities.rate.*
import com.gnoemes.shimori.data.base.entities.titles.anime.Anime
import com.gnoemes.shimori.data.base.entities.titles.anime.AnimeType
import com.gnoemes.shimori.data.base.entities.titles.anime.AnimeWithRate
import com.gnoemes.shimori.data.base.entities.titles.manga.Manga
import com.gnoemes.shimori.data.base.entities.titles.manga.MangaType
import com.gnoemes.shimori.data.base.entities.titles.manga.MangaWithRate
import com.gnoemes.shimori.data.base.entities.titles.ranobe.Ranobe
import com.gnoemes.shimori.data.base.entities.titles.ranobe.RanobeType
import com.gnoemes.shimori.data.base.entities.titles.ranobe.RanobeWithRate
import com.gnoemes.shimori.data.base.entities.user.User
import com.gnoemes.shimori.data.base.entities.user.UserShort
import com.gnoemes.shimori.data.base.mappers.Mapper
import com.gnoemes.shimori.data.base.mappers.TwoWayMapper
import comgnoemesshimoridatadb.QueryMeShort
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

internal object RateMapper : TwoWayMapper<RateDAO?, Rate?> {
    override suspend fun map(from: RateDAO?): Rate? {
        if (from == null) return null

        return Rate(
            id = from.id,
            shikimoriId = from.shikimori_id ?: 0,
            targetId = from.target_id,
            targetType = from.target_type,
            status = from.status,
            score = from.score,
            comment = from.comment,
            progress = from.progress,
            reCounter = from.re_counter,
            dateCreated = from.date_created,
            dateUpdated = from.date_updated,
        )
    }

    override suspend fun mapInverse(from: Rate?): RateDAO? {
        if (from == null) return null

        return RateDAO(
            id = from.id,
            shikimori_id = from.shikimoriId,
            target_id = from.targetId,
            target_type = from.targetType,
            status = from.status,
            score = from.score,
            comment = from.comment,
            progress = from.progress,
            re_counter = from.reCounter,
            date_created = from.dateCreated,
            date_updated = from.dateUpdated,
        )
    }
}

internal object UserMapper : TwoWayMapper<UserDAO?, User?> {
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

    override suspend fun mapInverse(from: User?): UserDAO? {
        if (from == null) return null

        return UserDAO(
            id = from.id,
            shikimori_id = from.shikimoriId,
            nickname = from.nickname,
            image_original = from.image?.original,
            image_preview = from.image?.preview,
            image_x96 = from.image?.x96,
            image_x48 = from.image?.x48,
            name = from.name,
            about = from.about,
            common_info = from.commonInfo,
            sex = from.sex,
            website = from.website,
            date_birth = from.dateBirth,
            locale = from.locale,
            full_years = from.fullYears,
            location = from.location,
            show_comments = from.showComments,
            friend = from.friend,
            ignored = from.ignored,
            banned = from.banned,
            last_online = from.lastOnlineAt,
            is_me = from.isMe
        )
    }
}

internal object RateSortMapper : TwoWayMapper<RateSortDAO?, RateSort?> {
    override suspend fun map(from: RateSortDAO?): RateSort? {
        if (from == null) return null

        return RateSort(
            id = from.id,
            type = ListType.findOrDefault(from.type),
            sortOption = from.sort,
            isDescending = from.descending
        )
    }

    override suspend fun mapInverse(from: RateSort?): RateSortDAO? {
        if (from == null) return null

        return RateSortDAO(
            id = from.id,
            type = from.type.type,
            sort = from.sortOption,
            descending = from.isDescending
        )
    }
}

internal object LastRequestMapper : TwoWayMapper<LastRequestDAO?, LastRequest?> {
    override suspend fun map(from: LastRequestDAO?): LastRequest? {
        if (from == null) return null

        return LastRequest(
            id = from.id,
            request = from.request,
            entityId = from.entity_id,
            timeStamp = from.timestamp
        )
    }

    override suspend fun mapInverse(from: LastRequest?): LastRequestDAO? {
        if (from == null) return null

        return LastRequestDAO(
            id = from.id,
            request = from.request,
            entity_id = from.entityId,
            timestamp = from.timeStamp
        )
    }
}

internal fun animeWithRate(
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
    next_episode: Int?,
    next_episode_date: Instant?,
    next_episode_end_date: Instant?,
    age_rating: AgeRating,
    duration: Int?,
    description: String?,
    description_html: String?,
    franchise: String?,
    favorite: Boolean,
    topic_id: Long?,
    genres: List<Genre>?,
    id_: Long,
    shikimori_id_: Long?,
    target_id: Long,
    target_type: RateTargetType,
    status_: RateStatus,
    score: Int?,
    comment: String?,
    progress: Int,
    re_counter: Int,
    date_created: Instant?,
    date_updated: Instant?
) = AnimeWithRate(
    anime(
        id, shikimori_id, name, name_ru, name_eng,
        image_original, image_preview, image_x96, image_x48,
        url, anime_type, rating, status, episodes, episodes_aired,
        date_aired, date_released, next_episode, next_episode_date, next_episode_end_date,
        age_rating, duration, description, description_html, franchise,
        favorite, topic_id, genres
    ),
    rate(
        id_, shikimori_id_, target_id, target_type, status_, score,
        comment, progress, re_counter, date_created, date_updated
    )
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
    next_episode: Int?,
    next_episode_date: Instant?,
    next_episode_end_date: Instant?,
    age_rating: AgeRating,
    duration: Int?,
    description: String?,
    description_html: String?,
    franchise: String?,
    favorite: Boolean,
    topic_id: Long?,
    genres: List<Genre>?,
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

internal fun animeDao(from: Anime) = AnimeDAO(
    id = from.id,
    shikimori_id = from.shikimoriId,
    name = from.name,
    name_ru = from.nameRu,
    name_eng = from.nameEn,
    image_original = from.image?.original,
    image_preview = from.image?.preview,
    image_x96 = from.image?.x96,
    image_x48 = from.image?.x48,
    url = from.url,
    anime_type = from.animeType?.type,
    rating = from.rating,
    status = from.status,
    episodes = from.episodes,
    episodes_aired = from.episodesAired,
    date_aired = from.dateAired,
    date_released = from.dateReleased,
    next_episode = from.nextEpisode,
    next_episode_date = from.nextEpisodeDate,
    next_episode_end_date = from.nextEpisodeEndDate,
    age_rating = from.ageRating,
    duration = from.duration,
    description = from.description,
    description_html = from.descriptionHtml,
    franchise = from.franchise,
    favorite = from.favorite,
    topic_id = from.topicId,
    genres = from.genres
)

internal fun mangaWithRate(
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
    id_: Long,
    shikimori_id_: Long?,
    target_id: Long,
    target_type: RateTargetType,
    status_: RateStatus,
    score: Int?,
    comment: String?,
    progress: Int,
    re_counter: Int,
    date_created: Instant?,
    date_updated: Instant?
) = MangaWithRate(
    manga(
        id, shikimori_id, name, name_ru, name_eng,
        image_original, image_preview, image_x96, image_x48,
        url, manga_type, rating, status, chapters, volumes,
        date_aired, date_released, age_rating, description,
        description_html, franchise, favorite, topic_id, genres
    ),
    rate(
        id_, shikimori_id_, target_id, target_type, status_,
        score, comment, progress, re_counter, date_created, date_updated
    )
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

internal fun mangaDao(from: Manga) = MangaDAO(
    id = from.id,
    shikimori_id = from.shikimoriId,
    name = from.name,
    name_ru = from.nameRu,
    name_eng = from.nameEn,
    image_original = from.image?.original,
    image_preview = from.image?.preview,
    image_x96 = from.image?.x96,
    image_x48 = from.image?.x48,
    url = from.url,
    manga_type = from.mangaType?.type,
    rating = from.rating,
    status = from.status,
    chapters = from.chapters,
    volumes = from.volumes,
    date_aired = from.dateAired,
    date_released = from.dateReleased,
    age_rating = from.ageRating,
    description = from.description,
    description_html = from.descriptionHtml,
    franchise = from.franchise,
    favorite = from.favorite,
    topic_id = from.topicId,
    genres = from.genres
)

internal fun ranobeWithRate(
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
    id_: Long,
    shikimori_id_: Long?,
    target_id: Long,
    target_type: RateTargetType,
    status_: RateStatus,
    score: Int?,
    comment: String?,
    progress: Int,
    re_counter: Int,
    date_created: Instant?,
    date_updated: Instant?
) = RanobeWithRate(
    ranobe(
        id, shikimori_id, name, name_ru, name_eng,
        image_original, image_preview, image_x96, image_x48,
        url, ranobe_type, rating, status, chapters, volumes,
        date_aired, date_released, age_rating, description,
        description_html, franchise, favorite, topic_id, genres
    ),
    rate(
        id_, shikimori_id_, target_id, target_type, status_,
        score, comment, progress, re_counter, date_created, date_updated
    )
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

internal fun ranobeDao(from: Ranobe) = RanobeDAO(
    id = from.id,
    shikimori_id = from.shikimoriId,
    name = from.name,
    name_ru = from.nameRu,
    name_eng = from.nameEn,
    image_original = from.image?.original,
    image_preview = from.image?.preview,
    image_x96 = from.image?.x96,
    image_x48 = from.image?.x48,
    url = from.url,
    ranobe_type = from.ranobeType?.type,
    rating = from.rating,
    status = from.status,
    chapters = from.chapters,
    volumes = from.volumes,
    date_aired = from.dateAired,
    date_released = from.dateReleased,
    age_rating = from.ageRating,
    description = from.description,
    description_html = from.descriptionHtml,
    franchise = from.franchise,
    favorite = from.favorite,
    topic_id = from.topicId,
    genres = from.genres
)

internal fun rate(
    id: Long,
    shikimori_id: Long?,
    target_id: Long,
    target_type: RateTargetType,
    status: RateStatus,
    score: Int?,
    comment: String?,
    progress: Int,
    re_counter: Int,
    date_created: Instant?,
    date_updated: Instant?
) = Rate(
    id = id,
    shikimoriId = shikimori_id ?: 0,
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