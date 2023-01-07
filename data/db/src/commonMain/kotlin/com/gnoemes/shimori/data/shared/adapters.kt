package com.gnoemes.shimori.data.shared

import com.gnoemes.shimori.data.core.entities.app.Request
import com.gnoemes.shimori.data.core.entities.app.SyncAction
import com.gnoemes.shimori.data.core.entities.app.SyncTarget
import com.gnoemes.shimori.data.core.entities.common.AgeRating
import com.gnoemes.shimori.data.core.entities.common.Genre
import com.gnoemes.shimori.data.core.entities.common.TitleStatus
import com.gnoemes.shimori.data.core.entities.rate.RateSortOption
import com.gnoemes.shimori.data.core.entities.rate.RateStatus
import com.gnoemes.shimori.data.core.entities.rate.RateTargetType
import com.squareup.sqldelight.ColumnAdapter
import comgnoemesshimoridatadb.*
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

internal object LocalDateAdapter : ColumnAdapter<LocalDate, String> {
    override fun decode(databaseValue: String) = LocalDate.parse(databaseValue)
    override fun encode(value: LocalDate) = value.toString()
}

internal object RateTargetAdapter : ColumnAdapter<RateTargetType, String> {
    override fun decode(databaseValue: String) = RateTargetType.valueOf(databaseValue)
    override fun encode(value: RateTargetType) = value.name
}

internal object RateStatusAdapter : ColumnAdapter<RateStatus, String> {
    override fun decode(databaseValue: String) = RateStatus.valueOf(databaseValue)
    override fun encode(value: RateStatus): String = value.name
}

internal object RateSortOptionAdapter : ColumnAdapter<RateSortOption, String> {
    override fun decode(databaseValue: String) = RateSortOption.valueOf(databaseValue)
    override fun encode(value: RateSortOption): String = value.name
}

internal object RequestAdapter : ColumnAdapter<Request, String> {
    override fun decode(databaseValue: String) = Request.valueOf(databaseValue)
    override fun encode(value: Request): String = value.name
}

internal object InstantAdapter : ColumnAdapter<Instant, Long> {
    override fun decode(databaseValue: Long) = Instant.fromEpochMilliseconds(databaseValue)
    override fun encode(value: Instant): Long = value.toEpochMilliseconds()
}

internal object TitleStatusAdapter : ColumnAdapter<TitleStatus, String> {
    override fun decode(databaseValue: String) = TitleStatus.valueOf(databaseValue)
    override fun encode(value: TitleStatus): String = value.name
}

internal object AgeRatingAdapter : ColumnAdapter<AgeRating, String> {
    override fun decode(databaseValue: String) = AgeRating.valueOf(databaseValue)
    override fun encode(value: AgeRating): String = value.name
}

internal object GenresAdapter : ColumnAdapter<List<Genre>, String> {

    override fun decode(databaseValue: String) = Json.decodeFromString<List<Genre>>(databaseValue)
    override fun encode(value: List<Genre>): String = Json.encodeToString(value)
}

internal object SyncTargetAdapter : ColumnAdapter<List<SyncTarget>, String> {
    override fun decode(databaseValue: String): List<SyncTarget> {
        return Json.decodeFromString(databaseValue)
    }

    override fun encode(value: List<SyncTarget>): String = Json.encodeToString(value)
}

internal object SyncActionAdapter : ColumnAdapter<SyncAction, String> {
    override fun decode(databaseValue: String) = SyncAction.valueOf(databaseValue)
    override fun encode(value: SyncAction): String = value.name
}


internal val RateAdapter = Rate.Adapter(
    target_typeAdapter = RateTargetAdapter,
    statusAdapter = RateStatusAdapter,
    date_createdAdapter = InstantAdapter,
    date_updatedAdapter = InstantAdapter
)

internal val UserAdapter = User.Adapter(
    last_onlineAdapter = InstantAdapter
)

internal val RateSortAdapter = Rate_sort.Adapter(
    sortAdapter = RateSortOptionAdapter
)

internal val LastRequestAdapter = Last_request.Adapter(
    requestAdapter = RequestAdapter,
    timestampAdapter = InstantAdapter
)

internal val AnimeAdapter = Anime.Adapter(
    statusAdapter = TitleStatusAdapter,
    date_airedAdapter = LocalDateAdapter,
    date_releasedAdapter = LocalDateAdapter,
    next_episode_dateAdapter = InstantAdapter,
    next_episode_end_dateAdapter = InstantAdapter,
    age_ratingAdapter = AgeRatingAdapter,
    genresAdapter = GenresAdapter
)

internal val MangaAdapter = Manga.Adapter(
    statusAdapter = TitleStatusAdapter,
    date_airedAdapter = LocalDateAdapter,
    date_releasedAdapter = LocalDateAdapter,
    age_ratingAdapter = AgeRatingAdapter,
    genresAdapter = GenresAdapter
)

internal val RanobeAdapter = Ranobe.Adapter(
    statusAdapter = TitleStatusAdapter,
    date_airedAdapter = LocalDateAdapter,
    date_releasedAdapter = LocalDateAdapter,
    age_ratingAdapter = AgeRatingAdapter,
    genresAdapter = GenresAdapter
)

internal val PinnedAdapter = Pinned.Adapter(
    target_typeAdapter = RateTargetAdapter
)

internal val RateToSyncAdapter = Rate_to_sync.Adapter(
    sync_targetsAdapter = SyncTargetAdapter,
    sync_actionAdapter = SyncActionAdapter,
    last_attemptAdapter = InstantAdapter
)

internal val CharacterRoleAdapter = Character_role.Adapter(
    target_typeAdapter = RateTargetAdapter,
)