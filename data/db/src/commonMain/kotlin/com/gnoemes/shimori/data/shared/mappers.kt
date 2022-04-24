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

internal object RateSortMapper : Mapper<RateSortDAO?, RateSort?> {
    override suspend fun map(from: RateSortDAO?): RateSort? {
        if (from == null) return null

        return RateSort(
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